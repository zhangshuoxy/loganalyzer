package com.officedepot.loganalyzer.processor.live;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

@Component
public class LiveLog {

	private Map<String, LinkedBlockingQueue<String>> exceptionQ = new HashMap<>();
	
	public String getLiveException(String server) {
		return getQueueOfServer(server).peek();
	}
	
	public List<String> getStackedLiveException(String server) {
		return Arrays.asList(getQueueOfServer(server).toArray(new String[0]));
	}

	public void addLiveLog(String server, String details) {
		getQueueOfServer(server).offer(details);
	}
	
	private LinkedBlockingQueue<String> getQueueOfServer(String server) {
		LinkedBlockingQueue<String> queue = exceptionQ.get(server);
		if (null == queue) {
			queue = new LinkedBlockingQueue<>(40);
			exceptionQ.put(server, queue);
		}
		return queue;
	}
}
