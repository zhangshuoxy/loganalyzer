package com.officedepot.loganalyzer.processor.logreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.officedepot.loganalyzer.LogAnalyzerConstants;
import com.officedepot.loganalyzer.domain.Server;
import com.officedepot.loganalyzer.processor.counter.ExceptionStatistic;

/**
 * Created by CH on 4/24/14.
 */
@Component
public class HttpLogReader extends LogReader {

    private static final Logger LOGGER = Logger.getLogger(HttpLogReader.class);
    private static final Logger PERF_LOGGER = Logger.getLogger(LogAnalyzerConstants.LOG_PERFORMANCE);

    private Map<String, Long> offsets = new HashMap<>();

    protected void read(Server server) {
    	String serverId = server.getName() + "-" + server.getNodeId();
    	ExceptionStatistic.countServerReadCount(serverId);
    	
    	StopWatch sw = null;
    	if (PERF_LOGGER.isDebugEnabled()) {
	    	sw = new StopWatch(serverId);
	    	sw.start();
    	}
    	
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(server.getLogURL());
        CloseableHttpResponse response = null;
        httpGet.setHeader("Accept-Encoding", "identity");
        httpGet.setHeader("Range", "bytes=0-");
        Long offset = offsets.get(serverId);
        if (null == offset) {
        	offset = 0l;
        }

        try {
            response = client.execute(httpGet);
            Header contentRangeHeader = response.getFirstHeader("Content-Range");
            String contentRangeValue = contentRangeHeader.getValue();
            long fileSize = getContentLength(contentRangeValue);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Log File Size From Server: " + fileSize);
            }
            response.close();

            if (fileSize == offset) {
                return;
            } else if (fileSize < offset) {
                offset = 0l;
                offsets.put(serverId, offset);
            }

            httpGet.setHeader("Range", "bytes=" + offset + "-");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Executing request " + httpGet.getRequestLine());
            }
            response = client.execute(httpGet);
            contentRangeHeader = response.getFirstHeader("Content-Range");
            contentRangeValue = contentRangeHeader.getValue();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Status Line: " + response.getStatusLine());
            }
            HttpEntity entity = response.getEntity();

            long downloadRange = getContentRangeSize(contentRangeValue);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Download Range: " + downloadRange);
            }

            if (entity != null) {
                InputStream is = null;
                BufferedReader reader = null;
                try {
                    is = entity.getContent();
                    reader = new BufferedReader(new InputStreamReader(is));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        super.save(serverId, line);
                    }
                    offset += downloadRange;
                    offsets.put(serverId, offset);
                } catch (IOException e) {
                    LOGGER.error(e);
                } finally {
                    if (null != reader) {
                        try {reader.close();} catch (IOException e) {}
                    }
                    if (null != is) {
                        try {is.close();} catch (IOException e) {}
                    }
                }
            }

            response.close();
        } catch (IOException e) {
            LOGGER.error(e);
        } finally {
        	if (null != httpGet) {
        		httpGet.releaseConnection();
        	}
            if (null != response) {
                try {response.close();} catch (IOException e) {}
            }
            if (PERF_LOGGER.isDebugEnabled()) {
            	sw.stop();
            	PERF_LOGGER.debug(sw.prettyPrint());
            }
        }
    }

    private long getContentRangeSize(String contentHeaderValue) {
        int fromIndex = contentHeaderValue.indexOf(" ");
        int toIndex = contentHeaderValue.indexOf("-");
        int slashIndex = contentHeaderValue.indexOf("/");
        String to = contentHeaderValue.substring(toIndex + 1, slashIndex);
        String from = contentHeaderValue.substring(fromIndex + 1, toIndex);
        long size = Long.valueOf(to) - Long.valueOf(from) + 1;
        return size;
    }

    private long getContentLength(String contentHeaderValue) {
        int slashIndex = contentHeaderValue.indexOf("/");
        String length = contentHeaderValue.substring(slashIndex + 1, contentHeaderValue.length());
        long size = Long.valueOf(length);
        return size;
    }
}
