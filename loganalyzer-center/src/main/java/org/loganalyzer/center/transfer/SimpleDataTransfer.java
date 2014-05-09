package org.loganalyzer.center.transfer;

import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingQueue;

public class SimpleDataTransfer {

	private static Hashtable<String, LinkedBlockingQueue<Object>> group = new Hashtable<String, LinkedBlockingQueue<Object>>();
	
	private final static int MAX_LENGTH_OF_ROAD = 10000;
	
	public static void put(String id, Object product){
		LinkedBlockingQueue<Object> pipe = group.get(id);
		if (pipe == null){
			createPipe(id);
			pipe = group.get(id);
		}
		try {
			pipe.put(product);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static Object get(String id) throws InterruptedException{
		LinkedBlockingQueue<Object> pipe = group.get(id);
		if (pipe == null) {
			createPipe(id);
			pipe = group.get(id);
		}
		return pipe.take();
	}
	
	private synchronized static void createPipe(String name){
		LinkedBlockingQueue<Object> pipe = group.get(name);
		if (pipe == null) {
			pipe = new LinkedBlockingQueue<Object>(MAX_LENGTH_OF_ROAD);
			group.put(name, pipe);
		}
	}
	
	public static void state(){
		for (String key : group.keySet()){
			LinkedBlockingQueue<Object> queue = group.get(key);
			System.out.println("gourp " + key + "used length:" + queue.size());
		}
	}
}
