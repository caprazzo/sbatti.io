package net.caprazzi.tools.sbatti.io;

import net.caprazzi.tools.sbatti.io.messageQueue.DataMessage;

public interface MessageCollector<TData> {
	public abstract void collect(DataMessage<TData> data);
}