package com.officedepot.loganalyzer.processor.counter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.mutable.MutableLong;

import com.officedepot.loganalyzer.domain.ExceptionEntry;

/**
 * Created by CH on 4/24/14.
 */
public class ExceptionStatistic {
	
    private static long exceptionToPersistQueueSize;
    private static long logToPersistQueueSize;
    private static long exceptionCounterToPersistQueueSize;
    private static Map<String, Map<String, MutableLong>> exceptionCounter = new ConcurrentHashMap<>();
    private static Map<String, MutableLong> serverReadCounter = new ConcurrentHashMap<>();

    public static long getExceptionToPersistQueueSize() {
        return exceptionToPersistQueueSize;
    }

    static void setExceptionToPersistQueueSize(long exceptionToPersistQueueSize) {
        ExceptionStatistic.exceptionToPersistQueueSize = exceptionToPersistQueueSize;
    }

    public static long getLogToPersistQueueSize() {
        return logToPersistQueueSize;
    }

    static void setLogToPersistQueueSize(long logToPersistQueueSize) {
        ExceptionStatistic.logToPersistQueueSize = logToPersistQueueSize;
    }

    public static long getExceptionCounterToPersistQueueSize() {
        return exceptionCounterToPersistQueueSize;
    }

    static void setExceptionCounterToPersistQueueSize(long exceptionCounterToPersistQueueSize) {
        ExceptionStatistic.exceptionCounterToPersistQueueSize = exceptionCounterToPersistQueueSize;
    }

    public static void countException(ExceptionEntry exception) {
        Map<String, MutableLong> exceptionCounts = getExceptionCounts(exception.getId().getServer());
        MutableLong count = new MutableLong(1);
        MutableLong oldCount = exceptionCounts.put(exception.getException(), count);
        if (null != oldCount) {
            count.add(oldCount.intValue());
        }
    }

    public static int getExceptionCount(String server, String exception) {
        Map<String, MutableLong> exceptionCounts = getExceptionCounts(server);
        return exceptionCounts.get(exception).intValue();
    }

    private static Map<String, MutableLong> getExceptionCounts(String server) {
        Map<String, MutableLong> exceptionCounts = exceptionCounter.get(server);
        if (null == exceptionCounts) {
            exceptionCounts = new ConcurrentHashMap<>();
            exceptionCounter.put(server, exceptionCounts);
        }
        return exceptionCounts;
    }

	public static Map<String, Map<String, MutableLong>> getExceptionCounter() {
		return exceptionCounter;
	}
	
	public static void countServerReadCount(String server) {
		MutableLong count = new MutableLong(1);
        MutableLong oldCount = serverReadCounter.put(server, count);
        if (null != oldCount) {
            count.add(oldCount.intValue());
        }
	}
	
	public static int getServerReadCount(String server) {
		return serverReadCounter.get(server).intValue();
	}

	public static Map<String, MutableLong> getServerReadCounter() {
		return serverReadCounter;
	}
	
}
