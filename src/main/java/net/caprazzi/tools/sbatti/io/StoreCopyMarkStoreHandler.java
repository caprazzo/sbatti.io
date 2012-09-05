package net.caprazzi.tools.sbatti.io;

import net.caprazzi.tools.sbatti.io.core.logging.Log;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessage;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageReceipt;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageStore;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class StoreCopyMarkStoreHandler<TData> implements DataMessageStore<TData> {
	
	private static final Log log = Log
			.forClass(StoreCopyMarkStoreHandler.class);
	
	private DataMessageStore<TData> firstStore;
	private DataMessageStore<TData> secondStore;

	public StoreCopyMarkStoreHandler(DataMessageStore<TData> firstStore, DataMessageStore<TData> secondStore) {
		this.firstStore = firstStore;
		this.secondStore = secondStore;
	}
	
	public ListenableFuture<DataMessageReceipt> store(String sender, final DataMessage<TData> capture) {
		
		log.debug("{}: Storing to first store {} (from {})", capture.getId(), firstStore, sender);
		
		ListenableFuture<DataMessageReceipt> tryFirstStore = firstStore.store(this.toString(), capture);
		
		Futures.addCallback(tryFirstStore, new ReceiptCallback() {
			@Override
			public void onComplete(DataMessageReceipt receipt) {
				if (!receipt.isSuccess()) {
					log.warn("{}: Failed to store to first store {}: {}", receipt.getCaptureId(),  firstStore, receipt);
				}
				else {
					log.debug("{}: Succesfully stored to first store {}", receipt.getCaptureId(), firstStore);
					storeToSecond(capture);
				}
			}
		});
		
		return tryFirstStore;		
	}
	
	private void confirmToFirstStore(DataMessageReceipt result) {
		firstStore.confirm(result);
	}
	
	private void storeToSecond(final DataMessage<TData> capture) {
		
		log.debug("{}: Storing to second store {}", capture.getId(), secondStore);
		
		ListenableFuture<DataMessageReceipt> trySecondStore
			= secondStore.store(
				"second-store", 
				capture);
		
		Futures.addCallback(trySecondStore, new ReceiptCallback() {
			@Override
			public void onComplete(DataMessageReceipt receipt) {
				if (!receipt.isSuccess()) { 
					log.warn("{}: Failed to store to second store {}: {}", receipt.getCaptureId(),  secondStore, receipt);
				}				
				else {
					log.debug("{}: Succesfully stored to second store {}", receipt.getCaptureId(), secondStore);
					confirmToFirstStore(receipt);
				}				
			}
		});
	}

	@Override
	public void confirm(DataMessageReceipt receipt) {
		
	}
	
	@Override
	public String toString() {
		return "store-copy( " + firstStore +" => "+ secondStore + " )";
	}	
	
}
