package net.caprazzi.tools.sbatti.io;

import java.util.concurrent.BlockingQueue;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class QueueingDataProbeStoreConsumer<TData> {

	private static int count;
	{
		count ++;
	}
	
	private QueueingDataProbe<TData> probe;
	private CaptureStore<TData> store;	
	private BlockingQueue<CapturedData<TData>> queue;
	private volatile boolean stopping;
	private StoreEventListener listener;

	public QueueingDataProbeStoreConsumer(QueueingDataProbe<TData> probe, CaptureStore<TData> store) {
		this.probe = probe;
		this.queue = probe.getQueue();
		this.store = store;
	}
	
	public void addListener(StoreEventListener listener) {
		this.listener = listener;		
	}

	public void start() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!stopping) {					
					try {
						CapturedData<TData> capture = queue.take();
						ListenableFuture<CaptureStoreReceipt> tryStore 
							= store.store(probe.toString(), capture);			
												
						Futures.addCallback(tryStore, new ReceiptCallback() {
							
							@Override
							public void onComplete(CaptureStoreReceipt receipt) {
								if (listener != null)
									listener.onComplete(receipt);
							}
						});

						
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}			
		});		
		thread.setName("Probe Consumer Thread #"+count);
		thread.start();
	}
	
	public void stop() {
		stopping = true;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
