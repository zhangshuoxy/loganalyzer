package com.officedepot.loganalyzer.processor.repository;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mvel2.MVEL;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.officedepot.loganalyzer.dao.ExceptionEntryDao;
import com.officedepot.loganalyzer.dao.MessageEntryDao;
import com.officedepot.loganalyzer.domain.ExceptionEntry;
import com.officedepot.loganalyzer.domain.IgnoreList;
import com.officedepot.loganalyzer.domain.MessageEntry;
import com.officedepot.loganalyzer.meta.LogAnalyzerMeta;
import com.officedepot.loganalyzer.processor.counter.ExceptionStatistic;

/**
 * Created by CH on 4/27/14.
 */
@Component
public class LogRepository {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(LogRepository.class);

    @Resource
    private MessageEntryDao messageEntryDao;

    @Resource
    private ExceptionEntryDao exceptionEntryDao;

    private LinkedBlockingQueue<MessageEntry> messageQ = new LinkedBlockingQueue<>(1024);
    private LinkedBlockingQueue<ExceptionEntry> exceptionQ = new LinkedBlockingQueue<>(1024);

    public void addMessage(MessageEntry message) {
    	if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("added: " + message); //$NON-NLS-1$
        }
        messageQ.offer(message);
    }

    public void addException(ExceptionEntry exception) {
    	if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("added: " + exception); //$NON-NLS-1$
        }
        exceptionQ.offer(exception);
        
        ExceptionStatistic.countException(exception);
    }

    @Async
    public void startProcessingMessageQueue() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Start Processing Message Queue..."); //$NON-NLS-1$
        }
        while (true) {
            try {
                if (messageQ.isEmpty()) {
                    continue;
                }
                MessageEntry message = messageQ.take();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("persisting message..." + message); //$NON-NLS-1$
                }

                messageEntryDao.save(message);
            } catch (InterruptedException e) {
                LOGGER.error("Failed to get ExceptionCount from queue", e);
            }
        }
    }

    @Async
    public void startProcessingExceptionQueue() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Start Processing Exception Queue..."); //$NON-NLS-1$
        }
        while (true) {
            try {
                if (messageQ.isEmpty()) {
                    continue;
                }
                ExceptionEntry exception = exceptionQ.take();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("persisting exception..." + exception); //$NON-NLS-1$
                }
                
                if (isPastException(exception)) {
                	LOGGER.info("SKIPPED past data");
                	continue;
                }

                if (!isInIgnoreList(exception)) {
                    ExceptionStatistic.countException(exception);
                    
                    exceptionEntryDao.save(exception);
                }
            } catch (InterruptedException e) {
                LOGGER.error("Failed to get ExceptionCount from queue", e);
            }
        }
    }

    private boolean isPastException(ExceptionEntry exception) {
    	Long lastExceptionTimestamp = LogAnalyzerMeta.getInstance().getLastExceptionTimestamp(exception.getId().getServer());
    	boolean isPastException = null == lastExceptionTimestamp ? false : lastExceptionTimestamp > exception.getId().getTimestamp();
    	
    	if (!isPastException) {
    		LogAnalyzerMeta.getInstance().setLastExceptionTimestamp(exception.getId().getServer(), exception.getId().getTimestamp());
    	}
    	
    	return isPastException;
	}

	private boolean isInIgnoreList(ExceptionEntry exception) {
        Set<IgnoreList> ignoreList = LogAnalyzerMeta.getInstance().getIgnoreList();
        if (null == ignoreList || ignoreList.isEmpty()) {
            return false;
        }
        for (IgnoreList ignore : ignoreList) {
            boolean exceptionNameMatches = exception.getException().equalsIgnoreCase(ignore.getException());
            if (!exceptionNameMatches) {
            	LOGGER.info("Ignored: " + exception);
                continue;
            }
            if (StringUtils.isEmpty(ignore.getExpression())) {
                return true;
            } else {
                try {
                    boolean fit = (boolean) MVEL.eval(ignore.getExpression(), exception.getMessage());
                    return fit;
                } catch (Exception e) {
                    LOGGER.warn("Invalid expression syntax", e);
                }
            }
        }

        return false;
    }
}
