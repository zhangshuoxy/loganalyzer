package org.loganalyzer.center.sink.wsimpl;

import java.util.ArrayList;
import java.util.List;

import org.loganalyzer.center.model.ILogEntity;
import org.loganalyzer.center.sink.ISink;
import org.loganalyzer.center.sink.ISinkManager;
import org.loganalyzer.center.transfer.SimpleDataTransfer;

public class WSSinkManager implements ISinkManager{

	private List<String> sinkClassNames = new ArrayList<String>();
	
	public List<String> getSinkClassNames() {
		return sinkClassNames;
	}

	public void setSinkClassNames(List<String> sinkClassNames) {
		this.sinkClassNames = sinkClassNames;
	}
	
	@Override
	public void sendEntity(ILogEntity logEntity){
		for (String n : sinkClassNames){
			SimpleDataTransfer.put(n, logEntity);
		}
	}

	@Override
	public void startAllSink() {
		for (String name : sinkClassNames){
			try {
				Class<?> clazz = Class.forName(name);
				ISink sink = (ISink)(clazz.newInstance());
				sink.start();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Sink: " + name + " start failed!");
			}
		}
	}

	@Override
	public void closeAllSink() {
		for (String name : sinkClassNames){
			try {
				Class<?> clazz = Class.forName(name);
				ISink sink = (ISink)(clazz.newInstance());
				sink.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Sink: " + name + " close failed!");
			}
		}
	}
	
}
