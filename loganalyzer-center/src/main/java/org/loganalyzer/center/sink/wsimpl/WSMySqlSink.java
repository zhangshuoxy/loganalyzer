package org.loganalyzer.center.sink.wsimpl;

import org.loganalyzer.center.model.ILogEntity;
import org.loganalyzer.center.processor.wsimpl.model.WSBasicFormatLogEntity;
import org.loganalyzer.center.sink.ISink;
import org.loganalyzer.center.transfer.SimpleDataTransfer;

public class WSMySqlSink implements ISink{

	@Override
	public void start() {
		Thread t1 = new Thread(new Runnable(){

			@Override
			public void run() {
				while (true) {
					Object o;
					try {
						o = SimpleDataTransfer.get(WSMySqlSink.class.getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
					ILogEntity entity = (ILogEntity)o;
					WSBasicFormatLogEntity wl = (WSBasicFormatLogEntity)(entity.getValue());
//					System.out.print(wl.getTimestamp() + " " + wl.getExceptionClass() + " " + wl.getMessage());
				}
			}
			
		});
		t1.start();              
	}

	@Override
	public void close() {
		System.out.println("My Sql Sink has closed.");
	}

}
