package org.loganalyzer.center.collector.wsimpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WsLogStreamSplitter {
	
	private WsLogStreamFormater formater;
	
	public WsLogStreamSplitter(WsLogStreamFormater formater){
		this.formater = formater;
	}
	
	
	private StringBuffer buffer = new StringBuffer();
	
	public void addStream(String line){
		boolean ignoreCurrentLine = checkAndIgnoreCurrentLine(line);
		if (ignoreCurrentLine || ignoreState) return;
		
		boolean checkNewEntityBegin = checkNewEntityBegin(line);
		if (checkNewEntityBegin && buffer.length() > 0) {
			bufferOut();
			buffer = new StringBuffer();
		} 
		buffer.append(line).append("\n");
	}
	
	
	private static final String START_DISPLAY_CURRENT_ENVIROMENT_STATEMENT = "************ Start Display Current Environment ************";
	private static final String END_DISPLAY_CURRENT_ENVIROMENT_STATEMENT = "************* End Display Current Environment *************";
	private boolean ignoreState = false;
	private int maxIgnoreLine = 0;
	
	private boolean checkAndIgnoreCurrentLine(String line){
		if (ignoreState) maxIgnoreLine++;
		
		if (START_DISPLAY_CURRENT_ENVIROMENT_STATEMENT.equalsIgnoreCase(line)){
			ignoreState = true;
			return true;
		}else if (END_DISPLAY_CURRENT_ENVIROMENT_STATEMENT.equalsIgnoreCase(line) || maxIgnoreLine > 11){
			ignoreState = false;
			maxIgnoreLine = 0;
			return true;
		}
		return false;
	}
	
	
	private static Pattern datePattern = Pattern.compile("^\\[\\d{1,2}/\\d{1,2}/\\d{1,2}\\s{1}\\d{1,2}:\\d{1,2}:\\d{1,2}:\\d{3}\\s{1}EDT\\].*");
	
	private boolean checkNewEntityBegin(String line){
		Matcher m = datePattern.matcher(line);
		return m.matches();
	}
	
	public void close(){
		if (buffer.length() > 0){
			bufferOut();
		}
	}
	
	private void bufferOut(){
		formater.out(buffer.toString());
	}
	
}
