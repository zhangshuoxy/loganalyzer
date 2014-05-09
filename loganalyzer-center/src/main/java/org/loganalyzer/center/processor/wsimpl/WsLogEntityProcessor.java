package org.loganalyzer.center.processor.wsimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.loganalyzer.center.collector.wsimpl.HttpPollingCollector;
import org.loganalyzer.center.model.ILogEntity;
import org.loganalyzer.center.processor.IFormater;
import org.loganalyzer.center.processor.IProcessor;
import org.loganalyzer.center.sink.ISinkManager;
import org.loganalyzer.center.transfer.SimpleDataTransfer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class WsLogEntityProcessor implements IProcessor {

	private List<Thread> threadPool = null;

	private int fixedThreadNum = 2;

	public WsLogEntityProcessor() {
		threadPool = new ArrayList<Thread>();
	}

	@Override
	public void start() {
		for (int i = 0; i < fixedThreadNum; i++) {
			ProcessThread pt = new ProcessThread(UUID.randomUUID().toString());
			pt.start();
			threadPool.add(pt);
		}
	}

	@Override
	public void close() {
		for (Thread t : threadPool) {
			t.interrupt();
		}
		System.out.println("WsLogEntityProcessor has been closed.");
	}

	class ProcessThread extends Thread {

		private ApplicationContext ac = new GenericXmlApplicationContext("sinks.xml");
		
		private ISinkManager sinkManager = null;
		
		ProcessThread(String name) {
			super(name);
			sinkManager = (ISinkManager)(ac.getBean("WsSinkManager"));
		}

		public void run() {
			while (true) {
				Object o;
				try {
					o = SimpleDataTransfer.get(HttpPollingCollector.class.getName());
				} catch (InterruptedException e) {
					System.out.println("Thread " + this.getId() + " has exited.");
					break;
				}
				ILogEntity oldEntity = (ILogEntity) o;
				IFormater formater = new WsLogEntityFormater();
				ILogEntity newEntity = formater.format(oldEntity);
				if (newEntity != null){
					sinkManager.sendEntity(newEntity);
				}
			}

		}
	}

}
