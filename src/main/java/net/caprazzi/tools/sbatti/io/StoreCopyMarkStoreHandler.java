package net.caprazzi.tools.sbatti.io;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class StoreCopyMarkStoreHandler<TData> implements CaptureStore<TData> {
	
	private static interface ReceiptCallback<TData>
		extends FutureCallback<CaptureStoreReceipt<TData>> {}
	
	private CaptureStore<TData> firstStore;
	private CaptureStore<TData> secondStore;

	public StoreCopyMarkStoreHandler(CaptureStore<TData> firstStore, CaptureStore<TData> secondStore) {
		this.firstStore = firstStore;
		this.secondStore = secondStore;
	}
	
	public ListenableFuture<CaptureStoreReceipt<TData>> store(String sender, CapturedData<TData> capture) {
		
		System.out.println("STORE " + capture);
		ListenableFuture<CaptureStoreReceipt<TData>> tryFirstStore = firstStore.store(sender, capture);
		
		Futures.addCallback(tryFirstStore, new ReceiptCallback<TData>() {

			@Override
			public void onSuccess(CaptureStoreReceipt<TData> result) {				
				storeToSecond(result);				
			}

			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
				// TODO log and propagate			
			}
			
		});
		
		return tryFirstStore;		
	}
	
	private void confirmToFirstStore(CaptureStoreReceipt<TData> result) {
		firstStore.confirm(result);
	}
	
	private void storeToSecond(CaptureStoreReceipt<TData> result) {
		System.out.println("STORE TO SECOND " + result);
		ListenableFuture<CaptureStoreReceipt<TData>> trySecondStore
			= secondStore.store(
				"second-store", 
				result.getCapture());
		
		Futures.addCallback(trySecondStore, new ReceiptCallback<TData>() {

			@Override
			public void onSuccess(CaptureStoreReceipt<TData> result) {
				System.out.println("CONFIRM TO FIRST " + result);
				confirmToFirstStore(result);			
			}

			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();		
			}
			
		});
	}

	@Override
	public void confirm(CaptureStoreReceipt<TData> receipt) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
