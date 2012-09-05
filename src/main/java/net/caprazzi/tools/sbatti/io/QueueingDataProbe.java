package net.caprazzi.tools.sbatti.io;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.caprazzi.tools.sbatti.io.messageQueue.DataMessage;

public class QueueingDataProbe<TData> implements MessageCollector<TData> {

	private final BlockingQueue<DataMessage<TData>> queue
		= new LinkedBlockingQueue<DataMessage<TData>>();
	
	private String name;
		
	public QueueingDataProbe(String name) {
		this.name = name;
	}
	
	@Override
	public void collect(DataMessage<TData> capture) {
		try {
			queue.put(capture);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BlockingQueue<DataMessage<TData>> getQueue() {
		return queue;
	}
	
	@Override
	public String toString() {
		return "QDP:" + name;
	}
	
}
