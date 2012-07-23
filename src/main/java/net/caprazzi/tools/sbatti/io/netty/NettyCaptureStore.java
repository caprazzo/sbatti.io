package net.caprazzi.tools.sbatti.io.netty;

import java.util.concurrent.Callable;

import net.caprazzi.tools.sbatti.io.CaptureStore;
import net.caprazzi.tools.sbatti.io.CaptureStoreReceipt;
import net.caprazzi.tools.sbatti.io.CapturedData;
import net.caprazzi.tools.sbatti.io.IDataSerializer;

import org.joda.time.Instant;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

public class NettyCaptureStore<TData> implements CaptureStore<TData> {

	
	private final ListeningExecutorService executor;
	private final IDataSerializer<TData> serializer;


	public NettyCaptureStore(ListeningExecutorService executor, IDataSerializer<TData> serializer) {
		this.executor = executor;
		this.serializer = serializer;		
	}
	
	
	@Override
	public ListenableFuture<CaptureStoreReceipt<TData>> store(String sender, final CapturedData<TData> capture) {
		return executor.submit(new Callable<CaptureStoreReceipt<TData>>() {
			@Override
			public CaptureStoreReceipt<TData> call() throws Exception {
				
				// to netty client
				
				return CaptureStoreReceipt.forSuccess(Instant.now(), capture);
			}
		});
	}

}
