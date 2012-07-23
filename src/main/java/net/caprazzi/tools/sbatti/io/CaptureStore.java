package net.caprazzi.tools.sbatti.io;

import com.google.common.util.concurrent.ListenableFuture;

public interface CaptureStore<TData> {

	public ListenableFuture<CaptureStoreReceipt<TData>> store(String sender, CapturedData<TData> capture);
	
}
