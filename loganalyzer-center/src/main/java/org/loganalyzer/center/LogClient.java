package org.loganalyzer.center;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.loganalyzer.center.collector.ICollector;
import org.loganalyzer.center.collector.wsimpl.HttpPollingCollector;
import org.loganalyzer.center.processor.IProcessor;
import org.loganalyzer.center.processor.wsimpl.WsLogEntityProcessor;
import org.loganalyzer.center.sink.ISinkManager;
import org.loganalyzer.center.transfer.SimpleDataTransfer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class LogClient {

	public static void main(String[] args) throws InterruptedException, IOException {
		ApplicationContext ac = new GenericXmlApplicationContext("sinks.xml");
		ISinkManager sinkManager = (ISinkManager) (ac.getBean("WsSinkManager"));
		sinkManager.startAllSink();
		IProcessor processor = new WsLogEntityProcessor();
		processor.start();
		ICollector collector = new HttpPollingCollector();
		collector.start();

		while (true) {
			InputStreamReader is_reader = new InputStreamReader(System.in);
			String str = new BufferedReader(is_reader).readLine();
			if (str != null){
				if (str.equals("close")){
					collector.close();
					processor.close();
					sinkManager.closeAllSink();
					System.exit(1);
				}else if (str.equals("ts")){
					SimpleDataTransfer.state();
				}
			}
		}
		
	}

}
