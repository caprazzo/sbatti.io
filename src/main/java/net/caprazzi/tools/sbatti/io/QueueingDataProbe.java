package net.caprazzi.tools.sbatti.io;

import java.util.concurrent.BlockingQueue;

public class QueueingDataProbe<TData> implements IDataProbe<TData> {

	private final BlockingQueue<CapturedData<TData>> queue;

	public QueueingDataProbe(BlockingQueue<CapturedData<TData>> queue) {
		this.queue = queue;
	}
		
	@Override
	public void capture(TData data) {
		try {
			queue.put(CapturedData.forData(data));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
