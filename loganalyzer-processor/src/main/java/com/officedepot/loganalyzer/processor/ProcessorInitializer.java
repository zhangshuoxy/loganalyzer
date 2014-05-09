package com.officedepot.loganalyzer.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.officedepot.loganalyzer.dao.ExceptionEntryDao;
import com.officedepot.loganalyzer.dao.IgnoreListDao;
import com.officedepot.loganalyzer.dao.ServerDao;
import com.officedepot.loganalyzer.domain.ExceptionEntry;
import com.officedepot.loganalyzer.domain.IgnoreList;
import com.officedepot.loganalyzer.domain.Server;
import com.officedepot.loganalyzer.meta.LogAnalyzerMeta;
import com.officedepot.loganalyzer.processor.repository.LogRepository;

/**
 * Created by CH on 4/24/14.
 */
@Component
public class ProcessorInitializer {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(ProcessorInitializer.class);

    @Resource
    private ServerDao serverDao;

    @Resource
    private IgnoreListDao ignoreListDao;

    @Resource
    private LogRepository logRepository;
    
    @Resource
    private ExceptionEntryDao exceptionEntryDao;
    
    @PostConstruct
    public void init() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("init() start");
        }

        loadLogSources();
        loadIgnoreList();
        loadLastExceptionTimestamp();

        logRepository.startProcessingMessageQueue();
        logRepository.startProcessingExceptionQueue();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("init() end");
        }
    }

	private void loadLogSources() {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("Loading Log Sources");
    	}
        Iterable<Server> servers = serverDao.findAll(new Sort("name", "nodeId"));
        if (null != servers && servers.iterator().hasNext()) {
            LogAnalyzerMeta.getInstance().setLogSources(Sets.newLinkedHashSet(servers));
            if (LOGGER.isDebugEnabled()) {
        		for (Server server : servers) {
        			LOGGER.debug("Loaded: " + server);
        		}
        	}
        }
    }

    private void loadIgnoreList() {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("Loading Ignore List");
    	}
        Iterable<IgnoreList> ignoreList = ignoreListDao.findAll(new Sort("exception", "expression"));
        if (null != ignoreList && ignoreList.iterator().hasNext()) {
            LogAnalyzerMeta.getInstance().setIgnoreList(Sets.newLinkedHashSet(ignoreList));
            if (LOGGER.isDebugEnabled()) {
        		for (IgnoreList ignore : ignoreList) {
        			LOGGER.debug("Loaded: " + ignore);
        		}
        	}
        }
    }

    private void loadLastExceptionTimestamp() {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("Loading Last Exception Timestamp");
    	}
    	Set<Server> servers = LogAnalyzerMeta.getInstance().getLogSources();
    	Map<String, Long> lastExceptionTimestamps = new HashMap<>();
    	for (Server server : servers) {
        	Pageable pageable = new PageRequest(0, 1, Sort.Direction.DESC, "id.timestamp");
            List<ExceptionEntry> entries = exceptionEntryDao.findByServer(server.getServerId(), pageable);
            if (null != entries && !entries.isEmpty()) {
            	lastExceptionTimestamps.put(server.getServerId(), entries.get(0).getId().getTimestamp());
            }
		}
    	
    	LogAnalyzerMeta.getInstance().setLastExceptionTimestamps(lastExceptionTimestamps);
	}
}
