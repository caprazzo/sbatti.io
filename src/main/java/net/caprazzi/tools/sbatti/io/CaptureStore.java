package net.caprazzi.tools.sbatti.io;

import com.google.common.util.concurrent.ListenableFuture;

public interface CaptureStore<TData> {

	public ListenableFuture<CaptureStoreReceipt> store(String sender, CapturedData<TData> capture);

	//TODO: move this to ConfirmableCaptureStore ?
	public void confirm(CaptureStoreReceipt receipt);	
	
}
