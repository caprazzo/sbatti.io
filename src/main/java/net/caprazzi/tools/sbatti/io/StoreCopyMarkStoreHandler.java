package net.caprazzi.tools.sbatti.io;

import net.caprazzi.tools.sbatti.io.core.logging.Log;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class StoreCopyMarkStoreHandler<TData> implements CaptureStore<TData> {
	
	private static final Log log = Log
			.forClass(StoreCopyMarkStoreHandler.class);
	
	private CaptureStore<TData> firstStore;
	private CaptureStore<TData> secondStore;

	public StoreCopyMarkStoreHandler(CaptureStore<TData> firstStore, CaptureStore<TData> secondStore) {
		this.firstStore = firstStore;
		this.secondStore = secondStore;
	}
	
	public ListenableFuture<CaptureStoreReceipt> store(String sender, final CapturedData<TData> capture) {
		
		log.debug("{}: Storing to first store {} (from {})", capture.getId(), firstStore, sender);
		
		ListenableFuture<CaptureStoreReceipt> tryFirstStore = firstStore.store(this.toString(), capture);
		
		Futures.addCallback(tryFirstStore, new ReceiptCallback() {
			@Override
			public void onComplete(CaptureStoreReceipt receipt) {
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
	
	private void confirmToFirstStore(CaptureStoreReceipt result) {
		firstStore.confirm(result);
	}
	
	private void storeToSecond(final CapturedData<TData> capture) {
		
		log.debug("{}: Storing to second store {}", capture.getId(), secondStore);
		
		ListenableFuture<CaptureStoreReceipt> trySecondStore
			= secondStore.store(
				"second-store", 
				capture);
		
		Futures.addCallback(trySecondStore, new ReceiptCallback() {
			@Override
			public void onComplete(CaptureStoreReceipt receipt) {
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
	public void confirm(CaptureStoreReceipt receipt) {
		
	}
	
	@Override
	public String toString() {
		return "store-copy( " + firstStore +" => "+ secondStore + " )";
	}	
	
}
