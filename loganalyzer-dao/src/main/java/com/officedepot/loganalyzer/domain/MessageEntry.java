package com.officedepot.loganalyzer.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "message_entry")
public class MessageEntry {

    @Id
    @EmbeddedId
    private EntryId id;
    
    @Column(length=500)
    private String message;

    public MessageEntry() {}

    public MessageEntry(String server, Date timestamp) {
        this.id = new EntryId(server, timestamp);
    }

    public MessageEntry(String server, Date timestamp, String message) {
        this(server, timestamp);
        this.message = message;
    }

    public EntryId getId() {
        return id;
    }

    public void setId(EntryId id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntry messageEntry = (MessageEntry) o;

        if (!id.equals(messageEntry.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "MessageEntry{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
