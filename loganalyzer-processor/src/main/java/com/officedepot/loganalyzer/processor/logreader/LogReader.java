package com.officedepot.loganalyzer.processor.logreader;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import com.officedepot.loganalyzer.domain.Server;
import com.officedepot.loganalyzer.meta.LogAnalyzerMeta;
import com.officedepot.loganalyzer.processor.parser.LogParser;

/**
 * Created by CH on 4/24/14.
 */
public abstract class LogReader {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(LogReader.class);
    
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @Resource
    private LogParser parser;

//    @Scheduled(fixedDelay = 5000)
    public void scheduled() {
        Set<Server> sources = LogAnalyzerMeta.getInstance().getLogSources();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("sources: " + sources);
        }
        if (null != sources && sources.size() > 0) {
            for (final Server server : sources) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        read(server);
                    }
                });
            }
        }
    }

    protected abstract void read(Server server);

    protected final void save(String server, String log) {
        parser.parse(server, log);
    }
}
