package com.officedepot.loganalyzer.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "ignore_entry")
public class IgnoreList {

    @Id
    private String id;
    private String exception;
    private String expression;

    public IgnoreList() {
    }

    public IgnoreList(String exception, String expression) {
        this.exception = exception;
        this.expression = expression;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IgnoreList that = (IgnoreList) o;

        if (exception != null ? !exception.equals(that.exception) : that.exception != null) return false;
        if (expression != null ? !expression.equals(that.expression) : that.expression != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = exception != null ? exception.hashCode() : 0;
        result = 31 * result + (expression != null ? expression.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IgnoreList{" +
                "id='" + id + '\'' +
                ", exception='" + exception + '\'' +
                ", expression='" + expression + '\'' +
                '}';
    }
}
