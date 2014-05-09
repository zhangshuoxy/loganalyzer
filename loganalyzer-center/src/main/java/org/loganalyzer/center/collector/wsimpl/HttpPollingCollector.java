package org.loganalyzer.center.collector.wsimpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.loganalyzer.center.collector.ICollector;
import org.loganalyzer.center.collector.wsimpl.model.Source;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HttpPollingCollector implements ICollector {

	private ApplicationContext ac = null;

	private List<Thread> threadPool = null;

	private long interval = 5000;
	
	public HttpPollingCollector(){
		ac = new GenericXmlApplicationContext("Sources.xml");
		threadPool = new ArrayList<Thread>();
	}
	
	@Override
	public void start() {
		Map<String, Source> m = ac.getBeansOfType(Source.class);
		for (Source s : m.values()) {
			PollingThread pt = new PollingThread(UUID.randomUUID().toString(), s);
			pt.start();
			threadPool.add(pt);
		}
	}

	@Override
	public void close() {
		for (Thread t : threadPool){
			t.interrupt();
		}
		System.out.println("HttpPollingCollector has been closed.");
	}

	
	class PollingThread extends Thread {

		private Source s = null;

		private CloseableHttpClient httpclient = HttpClients.createDefault();

		private long lastContentLength = 0;

		PollingThread(String name, Source s) {
			super(name);
			this.s = s;
		}

		public void run() {
			try {
				pollingRead();
			} catch (IOException e) {
				e.printStackTrace();
				this.interrupt();
				System.err.println("Thread " + this.getId() + " has exited.");
			} catch (InterruptedException e){
				System.out.println("Thread " + this.getId() + " has exited.");
			}
		}

		private void pollingRead() throws InterruptedException, IOException {
			HttpGet httpget = new HttpGet(s.getHost());
			httpget.setHeader("Accept-Encoding", "identity");

			while (true) {
				Thread.sleep(interval);

				CloseableHttpResponse response = httpclient.execute(httpget);
				InputStream is = response.getEntity().getContent();
				long contentLength = response.getEntity().getContentLength();

				if (contentLength == lastContentLength)
					continue;

				if (contentLength > lastContentLength) {
					is.skip(lastContentLength);
				}
				// begin to read;
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line = null;
				WsLogStreamSplitter splitter = new WsLogStreamSplitter(new WsLogStreamFormater(s));
				while ((line = reader.readLine()) != null) {
					splitter.addStream(line);
				}
				splitter.close();
				reader.close();
				is.close();
				lastContentLength = contentLength;
			}
		}
	}

}
