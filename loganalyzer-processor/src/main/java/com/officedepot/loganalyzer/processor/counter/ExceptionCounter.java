package com.officedepot.loganalyzer.processor.counter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.mutable.MutableLong;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.officedepot.loganalyzer.dao.ExceptionCountDao;
import com.officedepot.loganalyzer.domain.ExceptionCount;

/**
 * Created by CH on 4/23/14.
 */
@Service
public class ExceptionCounter {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(ExceptionCounter.class);
    
    @Resource
    private ExceptionCountDao dao;
    
    @Scheduled(cron = "0 0,10,20,30,40,50 * * * *")//every 10 minutes
    public void persistExceptionCounter() {
    	if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("persistExceptionCounter..."); //$NON-NLS-1$
        }
    	
    	Map<String, Map<String, MutableLong>> exceptionCounter = new HashMap<String, Map<String, MutableLong>>(ExceptionStatistic.getExceptionCounter());
    	ExceptionStatistic.getExceptionCounter().clear();
    	
    	Long timestamp = new Date().getTime();
    	
    	if (null == exceptionCounter || exceptionCounter.isEmpty()) {
    		return;
    	}
    	
    	List<ExceptionCount> exceptionCounters = new ArrayList<>();
        for (Entry<String, Map<String, MutableLong>> serverExceptionCounts : exceptionCounter.entrySet()) {
        	String server = serverExceptionCounts.getKey();
        	Map<String, MutableLong> exceptionCounts = serverExceptionCounts.getValue();
        	if (null == exceptionCounts || exceptionCounts.isEmpty()) {
        		continue;
        	}
        	
        	for (Entry<String, MutableLong> exceptionCount : exceptionCounts.entrySet()) {
        		String exception = exceptionCount.getKey();
        		Long count = exceptionCount.getValue().longValue();
        		exceptionCounters.add(new ExceptionCount(server, exception, count, timestamp));
        	}
		}
        
        dao.save(exceptionCounters);
    }
}
