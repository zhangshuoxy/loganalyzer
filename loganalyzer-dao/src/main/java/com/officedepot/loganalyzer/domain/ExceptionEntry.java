package com.officedepot.loganalyzer.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exception_entry")
public class ExceptionEntry {

    @EmbeddedId
    private EntryId id;
    private String exception;
    private String location;
    
    @Column(length=1000)
    private String message;
    
    @Column(length=10000)
    private String details;

    public ExceptionEntry() {}

    public ExceptionEntry(String server, Date timestamp, String exception, String location, String message, String details) {
        this.id = new EntryId(server, timestamp);
        this.exception = exception;
        this.location = location;
        this.message = message;
        this.details = details;
    }

    public ExceptionEntry(String server, String exception) {
        this.id = new EntryId(server);
        this.exception = exception;
    }

    public EntryId getId() {
        return id;
    }

    public void setId(EntryId id) {
        this.id = id;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExceptionEntry that = (ExceptionEntry) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ExceptionEntry{" +
                "id='" + id + '\'' +
                ", exception='" + exception + '\'' +
                ", location='" + location + '\'' +
                ", message='" + message + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
