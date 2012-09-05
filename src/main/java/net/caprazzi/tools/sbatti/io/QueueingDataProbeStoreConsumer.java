package net.caprazzi.tools.sbatti.io;

import java.util.concurrent.BlockingQueue;

import net.caprazzi.tools.sbatti.io.messageQueue.DataMessage;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageReceipt;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageStore;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class QueueingDataProbeStoreConsumer<TData> {

	private static int count;
	{
		count ++;
	}
	
	private QueueingDataProbe<TData> probe;
	private DataMessageStore<TData> store;	
	private BlockingQueue<DataMessage<TData>> queue;
	private volatile boolean stopping;
	private StoreEventListener listener;

	public QueueingDataProbeStoreConsumer(QueueingDataProbe<TData> probe, DataMessageStore<TData> store) {
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
						DataMessage<TData> capture = queue.take();
						ListenableFuture<DataMessageReceipt> tryStore 
							= store.store(probe.toString(), capture);			
												
						Futures.addCallback(tryStore, new ReceiptCallback() {
							
							@Override
							public void onComplete(DataMessageReceipt receipt) {
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
