package org.loganalyzer.center.processor.wsimpl.model;

import java.util.Set;


public class WSBasicFormatLogEntity{

	private long timestamp;
	
	private String threadId;
	
	private String shortName;
	
	private String eventType; //F,E,W,A,I,C,D,O,R,Z

	private String message;

	private Set<String> exceptions;
	
	private String exceptionClass;
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<String> getExceptions() {
		return exceptions;
	}

	public void setExceptions(Set<String> exceptions) {
		this.exceptions = exceptions;
	}

	public String getExceptionClass() {
		return exceptionClass;
	}

	public void setExceptionClass(String exceptionClass) {
		this.exceptionClass = exceptionClass;
	}
	
}
