package net.caprazzi.tools.sbatti.io;

import java.util.concurrent.BlockingQueue;

public class QueueingDataProbeStoreConsumer<TData> {

	private QueueingDataProbe<TData> probe;
	private CaptureStore<TData> store;	
	private BlockingQueue<CapturedData<TData>> queue;
	private volatile boolean stopping;

	public QueueingDataProbeStoreConsumer(QueueingDataProbe<TData> probe, CaptureStore<TData> store) {
		this.probe = probe;
		this.queue = probe.getQueue();
		this.store = store;
	}

	public void start() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!stopping) {					
					try {
						// TODO: make take time out after some time and log possible problem
						CapturedData<TData> capture = queue.take();
						// TODO: use informative, dynamic naming here
						store.store("queue-probe-consumer", capture);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}			
		});		
		thread.setName(this.getClass().getName());
		thread.start();
	}
	
	public void stop() {
		stopping = true;
	}
	
}
