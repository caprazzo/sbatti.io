package net.caprazzi.tools.sbatti.io;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueingDataProbe<TData> implements IDataProbe<TData> {

	private final BlockingQueue<CapturedData<TData>> queue
		= new LinkedBlockingQueue<CapturedData<TData>>();
	
	private String name;
		
	public QueueingDataProbe(String name) {
		this.name = name;
	}
	
	@Override
	public void capture(CapturedData<TData> capture) {
		try {
			queue.put(capture);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BlockingQueue<CapturedData<TData>> getQueue() {
		return queue;
	}
	
	@Override
	public String toString() {
		return "QDP:" + name;
	}
	
}
