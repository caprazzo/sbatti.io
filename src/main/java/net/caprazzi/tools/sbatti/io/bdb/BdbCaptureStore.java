package net.caprazzi.tools.sbatti.io.bdb;

import java.util.concurrent.Callable;

import net.caprazzi.tools.sbatti.io.CaptureStore;
import net.caprazzi.tools.sbatti.io.CaptureStoreReceipt;
import net.caprazzi.tools.sbatti.io.CapturedData;
import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.core.logging.Log;

import org.joda.time.Instant;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

public class BdbCaptureStore<TData> implements CaptureStore<TData> {
	
	private static final Log log = Log
			.forClass(BdbCaptureStore.class);
	
	private final BdbCaptureDao<TData> dao;
	private final ListeningExecutorService executor;
	private final IDataSerializer<TData> serializer;

	public BdbCaptureStore(ListeningExecutorService executor, BdbCaptureDao<TData> dao, IDataSerializer<TData> serializer) {
		this.executor = executor;
		this.dao = dao;
		this.serializer = serializer;		
	}
	
	@Override
	public ListenableFuture<CaptureStoreReceipt> store(final String sender, final CapturedData<TData> capture) {
		
		log.debug("{}: Storing to {} (from {})", capture.getId(), dao, sender);		
		
		return executor.submit(new Callable<CaptureStoreReceipt>() {
			@Override
			public CaptureStoreReceipt call() throws Exception {
				// TODO: get sender from the capture
				dao.save(
					sender, 
					capture.getId(),
					capture.getTimestamp(), 
					serializer.serialize(capture.getData()));
				
				CaptureStoreReceipt receipt
					= CaptureStoreReceipt.forSuccess(capture.getId(), Instant.now(), "Saved in " + dao);
				
				log.debug("{}: {} while storing to {}", receipt.getCaptureId(), receipt.isSuccess() ? "" + "SUCCESS" : "FAILURE", dao);
				
				return receipt;
			}
		});
	}

	@Override
	public void confirm(CaptureStoreReceipt receipt) {
		log.debug("{}: Confirmed by {}", receipt.getCaptureId(), "TODO-SENDER");
		dao.confirm(receipt.getCaptureId());		
	}
	
	@Override
	public String toString() {
		return "bdb-store";
	}

}
