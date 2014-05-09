package com.officedepot.loganalyzer.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.joda.time.DateTime;

import com.officedepot.loganalyzer.LogAnalyzerConstants;

/**
 * Created by CH on 4/23/14.
 */
@Entity
@Table(name = "exception_count")
public class ExceptionCount {

    @Id
    private String id;
    private String server;
    private String exception;
    private Long count;
    private Long timestamp;

    public ExceptionCount() {
    }

    public ExceptionCount(String server, Long timestamp, Long count) {
		this.server = server;
		this.timestamp = timestamp;
		this.count = count;
	}

	public ExceptionCount(String server, String exception, Long count, Long timestamp) {
        this.server = server;
        this.exception = exception;
        this.count = count;
        this.timestamp = timestamp;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString();
        if (null == timestamp) {
            timestamp = new Date().getTime();
        }
    }

    public void increaseCount() {
        this.id = null;
        count += 1;
        timestamp = new Date().getTime();
    }

    public void increaseCount(long delta) {
        this.id = null;
        count += delta;
        timestamp = new Date().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getTimestamp() {
        return timestamp;
    }
    
    public String getTimeString() {
    	return new DateTime(timestamp).toString(LogAnalyzerConstants.FORMATTER_TIMESTAMP);
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ExceptionCount{" +
                "id='" + id + '\'' +
                ", server='" + server + '\'' +
                ", exception='" + exception + '\'' +
                ", count=" + count +
                ", timestamp=" + getTimeString() +
                '}';
    }
}
