package com.officedepot.loganalyzer.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;

import org.joda.time.DateTime;

import com.officedepot.loganalyzer.LogAnalyzerConstants;

@Embeddable
public class EntryId implements Serializable {

	private static final long serialVersionUID = 6170949907982457393L;

    private String server;
    private Long timestamp;

    public EntryId() {
    }

    public EntryId(String server, Date timestamp) {
        this.server = server;
        this.timestamp = timestamp.getTime();
    }

    public EntryId(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Long getTimestamp() {
        return timestamp;
    }
    
    public String getTimeString() {
    	return new DateTime(timestamp).toString(LogAnalyzerConstants.FORMATTER_DATE);
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntryId entryId = (EntryId) o;

        if (!server.equals(entryId.server)) return false;
        if (!timestamp.equals(entryId.timestamp)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = server.hashCode();
        result = 31 * result + timestamp.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EntryId{" +
                "server='" + server + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
