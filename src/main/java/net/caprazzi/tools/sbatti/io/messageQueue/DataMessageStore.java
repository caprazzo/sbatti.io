package net.caprazzi.tools.sbatti.io.messageQueue;


import com.google.common.util.concurrent.ListenableFuture;

public interface DataMessageStore<TData> {

	public ListenableFuture<DataMessageReceipt> store(String sender, DataMessage<TData> capture);

	//TODO: move this to ConfirmableCaptureStore ?
	public void confirm(DataMessageReceipt receipt);	
	
}
