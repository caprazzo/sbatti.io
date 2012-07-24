package net.caprazzi.tools.sbatti.io;

import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageReceipt;

import com.google.common.util.concurrent.FutureCallback;

public abstract class ReceiptCallback implements FutureCallback<DataMessageReceipt> {
	
	@Override
	public final void onFailure(Throwable t) {
		onComplete(DataMessageReceipt.forFailure(t));		
	}

	@Override
	public final void onSuccess(DataMessageReceipt receipt) {
		onComplete(receipt);
	}
	
	public abstract void onComplete(DataMessageReceipt receipt);

}
