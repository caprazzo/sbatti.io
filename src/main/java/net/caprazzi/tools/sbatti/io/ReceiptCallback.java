package net.caprazzi.tools.sbatti.io;

import com.google.common.util.concurrent.FutureCallback;

public abstract class ReceiptCallback implements FutureCallback<CaptureStoreReceipt> {
	
	@Override
	public final void onFailure(Throwable t) {
		onComplete(CaptureStoreReceipt.forFailure(t));		
	}

	@Override
	public final void onSuccess(CaptureStoreReceipt receipt) {
		onComplete(receipt);
	}
	
	public abstract void onComplete(CaptureStoreReceipt receipt);

}
