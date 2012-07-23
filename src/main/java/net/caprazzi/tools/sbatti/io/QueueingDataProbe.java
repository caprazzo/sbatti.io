package net.caprazzi.tools.sbatti.io;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueingDataProbe<TData> implements IDataProbe<TData> {

	private final BlockingQueue<CapturedData<TData>> queue
		= new LinkedBlockingQueue<CapturedData<TData>>();
		
	@Override
	public void capture(TData data) {
		try {
			queue.put(CapturedData.forData(data));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BlockingQueue<CapturedData<TData>> getQueue() {
		return queue;
	}
	
}
