package org.loganalyzer.center.collector.wsimpl;

import org.loganalyzer.center.collector.wsimpl.model.Source;
import org.loganalyzer.center.model.wsimpl.WSLogEntity;
import org.loganalyzer.center.model.wsimpl.WSLogMeta;
import org.loganalyzer.center.transfer.SimpleDataTransfer;

public class WsLogStreamFormater {

	private Source source;
	
	public WsLogStreamFormater(Source source){
		this.source = source;
	}
	
	public void out(String value){
		WSLogEntity e = new WSLogEntity();
		WSLogMeta m = new WSLogMeta();
		m.setSource(source);
		e.setMeta(m);
		e.setValue(value);
		SimpleDataTransfer.put(HttpPollingCollector.class.getName(), e);
	}
}
