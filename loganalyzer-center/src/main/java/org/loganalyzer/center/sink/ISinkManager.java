package org.loganalyzer.center.sink;

import org.loganalyzer.center.model.ILogEntity;

public interface ISinkManager {

	public void startAllSink();
	
	public void sendEntity(ILogEntity logEntity);
	
	public void closeAllSink();
	
}
