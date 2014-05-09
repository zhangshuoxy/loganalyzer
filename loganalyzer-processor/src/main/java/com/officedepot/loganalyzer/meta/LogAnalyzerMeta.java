package com.officedepot.loganalyzer.meta;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.officedepot.loganalyzer.domain.IgnoreList;
import com.officedepot.loganalyzer.domain.Server;

/**
 * Created by CH on 4/25/14.
 */
public class LogAnalyzerMeta {

    private static LogAnalyzerMeta INSTANCE = new LogAnalyzerMeta();

    private Set<Server> logSources = new LinkedHashSet<>();
    private Set<IgnoreList> ignoreList = new LinkedHashSet<>();
    
    private Map<String, Long> lastExceptionTimestamps = new HashMap<>();

    private LogAnalyzerMeta() {}

    public static LogAnalyzerMeta getInstance() {
        return INSTANCE;
    }

    public Set<Server> getLogSources() {
        return logSources;
    }

    public void setLogSources(Set<Server> logSources) {
        this.logSources = logSources;
    }

    public Set<IgnoreList> getIgnoreList() {
        return ignoreList;
    }

    public void setIgnoreList(Set<IgnoreList> ignoreList) {
        this.ignoreList = ignoreList;
    }
    
    public void setLastExceptionTimestamp(String server, Long timestamp) {
    	lastExceptionTimestamps.put(server, timestamp);
    }
    
    public Long getLastExceptionTimestamp(String server) {
    	return lastExceptionTimestamps.get(server);
    }

	public Map<String, Long> getLastExceptionTimestamps() {
		return lastExceptionTimestamps;
	}

	public void setLastExceptionTimestamps(Map<String, Long> lastExceptionTimestamps) {
		this.lastExceptionTimestamps = lastExceptionTimestamps;
	}
}
