package net.caprazzi.tools.sbatti.io.bdb;

import java.util.concurrent.Callable;

import net.caprazzi.tools.sbatti.io.CaptureStore;
import net.caprazzi.tools.sbatti.io.CaptureStoreReceipt;
import net.caprazzi.tools.sbatti.io.CapturedData;
import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.example.InterestingObject;

import org.joda.time.Instant;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

public class BdbCaptureStore<TData> implements CaptureStore<TData> {
	
	private final BdbCaptureDao<TData> dao;
	private final ListeningExecutorService executor;
	private final IDataSerializer<TData> serializer;

	public BdbCaptureStore(ListeningExecutorService executor, BdbCaptureDao<TData> dao, IDataSerializer<TData> serializer) {
		this.executor = executor;
		this.dao = dao;
		this.serializer = serializer;		
	}
	
	@Override
	public ListenableFuture<CaptureStoreReceipt<TData>> store(final String sender, final CapturedData<TData> capture) {
		return executor.submit(new Callable<CaptureStoreReceipt<TData>>() {
			@Override
			public CaptureStoreReceipt<TData> call() throws Exception {
				dao.save(
					sender, 
					capture.getId(),
					capture.getTimestamp(), 
					serializer.serialize(capture.getData()));
				return CaptureStoreReceipt.forSuccess(Instant.now(), capture);
			}
		});
	}

	public void delete(CaptureStoreReceipt<InterestingObject> receipt) {
		System.out.println("Deleting confirmed receipt " + receipt);
	}

}
