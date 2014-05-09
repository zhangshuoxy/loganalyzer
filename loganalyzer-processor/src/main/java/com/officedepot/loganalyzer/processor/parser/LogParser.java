package com.officedepot.loganalyzer.processor.parser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.officedepot.loganalyzer.LogAnalyzerConstants;
import com.officedepot.loganalyzer.domain.ExceptionEntry;
import com.officedepot.loganalyzer.domain.MessageEntry;
import com.officedepot.loganalyzer.processor.live.LiveLog;
import com.officedepot.loganalyzer.processor.repository.LogRepository;

/**
 * Created by CH on 4/25/14.
 */
@Component
public class LogParser {

    private static final Logger LOGGER = Logger.getLogger(LogParser.class);
    private static final Logger PERF_LOGGER = Logger.getLogger(LogAnalyzerConstants.LOG_PERFORMANCE);

    private static final Pattern PATTERN_BEGINNING = Pattern.compile("\\[([^]]+).*[\\s\\[]((?:[a-zA-Z]+)(?:\\.[a-zA-Z]*)+)[\\]\\s\\-]+(.*)");
    private static final Pattern PATTERN_EXCEPTION = Pattern.compile("([\\w\\.]+(?:Exception|Error)):(.*)");
    private static final Pattern PATTERN_AT_COM_IBM_WS = Pattern.compile("\\s+at\\scom.ibm.ws.*");
    private static final Pattern PATTERN_AT_COM_IBM_IO = Pattern.compile("\\s+at\\scom.ibm.io.*");
    private static final Pattern PATTERN_CAUSED_BY = Pattern.compile("[\\w\\s]+:\\s([\\w\\.]+(?:Exception|Error)):(.*)");

    private static final DateTimeFormatter FORMATTER_TIME_ZONE = DateTimeFormat.forPattern("M/d/yy HH:mm:ss:SSS zzz");

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Map<String, LinkedBlockingQueue<String>> queues = new HashMap<>();

    @Resource
    private LogRepository logRepository;
    
    @Resource
	private LiveLog liveLog;

    public void parse(String server, String line) {
        getQueue(server).offer(line);
    }

    private synchronized LinkedBlockingQueue<String> getQueue(String server) {
        LinkedBlockingQueue<String> queue = queues.get(server);
        if (null == queue) {
            queue = new LinkedBlockingQueue<String>(1024);
            queues.put(server, queue);
            createParser(server, queue);
        }
        return queue;
    }

    private void createParser(final String server, final LinkedBlockingQueue<String> queue) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                parsing(server, queue);
            }
        });
    }

    private void parsing(String server, LinkedBlockingQueue<String> queue) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Starting parser for: " + server); //$NON-NLS-1$
        }

        StringBuilder chunk = new StringBuilder(100);
        Date timestamp = null;
        String location = null;
        String msg = null;
        String exception = null;
        String details = null;

        while (true) {
            if (queue.isEmpty()) {
            	try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
                continue;
            }

            try {
                StopWatch sw = null;
            	if (PERF_LOGGER.isDebugEnabled()) {
        	    	sw = new StopWatch(server);
        	    	sw.start();
            	}
                String line = queue.take();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[parsing] " + line); //$NON-NLS-1$
                }

                Matcher msgMatcher = PATTERN_BEGINNING.matcher(line);
                boolean isChunkEmpty = 0 == chunk.length();
                boolean isNewChunkStart = msgMatcher.matches();

                if (!isChunkEmpty || isNewChunkStart) {
                    if (isNewChunkStart) {
                        //save
                    	if (chunk.length() > 0) {
                    		details = chunk.toString();
                    		save(server, timestamp, msg, exception, location, details);
                    		
                    		chunk.setLength(0);
                    	}
                        
                        String timestampString = msgMatcher.replaceAll("$1");
                        location = msgMatcher.replaceAll("$2");
                        msg = msgMatcher.replaceAll("$3");
                        timestamp = DateTime.parse(timestampString, FORMATTER_TIME_ZONE).toDate();

                        exception = null;
                        details = null;
                    }

                    if (chunk.length() < 9000 && !PATTERN_AT_COM_IBM_WS.matcher(line).matches() && !PATTERN_AT_COM_IBM_IO.matcher(line).matches()) {
                    	chunk.append(line).append(LINE_SEPARATOR);
                    	
                        Matcher exceptionMatcher = PATTERN_EXCEPTION.matcher(line);
                        if (exceptionMatcher.matches()) {
                        	exception = exceptionMatcher.replaceAll("$1");
                        	msg += exceptionMatcher.replaceAll("$2");
                        } else if ((exceptionMatcher = PATTERN_CAUSED_BY.matcher(line)).matches()) {
                        	exception = exceptionMatcher.replaceAll("$1");
                        	msg += exceptionMatcher.replaceAll("$2");
                        }
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.error("Failed to get log lines from queue", e);
            }
        }
    }

    private void save(String server, Date timestamp, String msg, String exception, String location, String details) {
        if (StringUtils.isEmpty(server) || null == timestamp || StringUtils.isEmpty(msg)) {
        	if (LOGGER.isDebugEnabled()) {
        		LOGGER.debug("Skipped saving, invalid data:");
        		LOGGER.debug(details);
        		LOGGER.debug("=========================.");
        	}
            return;
        }
        
        if (StringUtils.isNotBlank(exception)) {
            logRepository.addException(new ExceptionEntry(server, timestamp, exception, location, msg, details));
        	liveLog.addLiveLog(server, details);
        } else {
            logRepository.addMessage(new MessageEntry(server, timestamp, msg));
        }
    }
}
