package net.caprazzi.tools.sbatti.io.netty;

import java.util.concurrent.Callable;

import net.caprazzi.tools.sbatti.io.Capture;
import net.caprazzi.tools.sbatti.io.Capture.CapturedReceipt;
import net.caprazzi.tools.sbatti.io.CaptureStore;
import net.caprazzi.tools.sbatti.io.CaptureStoreReceipt;
import net.caprazzi.tools.sbatti.io.CapturedData;
import net.caprazzi.tools.sbatti.io.IDataSerializer;

import org.joda.time.Instant;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.protobuf.ByteString;

public class NettyCaptureStore<TData> implements CaptureStore<TData> {
	
	private final ListeningExecutorService executor;
	private final IDataSerializer<TData> serializer;
	private NettyCaptureStoreClient client;
	
	public NettyCaptureStore(ListeningExecutorService executor, IDataSerializer<TData> serializer, NettyCaptureStoreClient client) {
		this.executor = executor;
		this.serializer = serializer;
		this.client = client;		
	}
	
	
	@Override
	public ListenableFuture<CaptureStoreReceipt<TData>> store(final String sender, final CapturedData<TData> capture) {
		return executor.submit(new Callable<CaptureStoreReceipt<TData>>() {
			@Override
			public CaptureStoreReceipt<TData> call() throws Exception {
				
				// to netty client
				
				System.out.println(" SEND TO NETTY " + sender);
				
				CapturedReceipt receipt = client.send(Capture.Captured
						.newBuilder()
						.setData(ByteString.copyFrom(serializer.serialize(capture.getData())))
						.setId(capture.getId().toString())
						.setSender(sender)
						.setTimestamp(capture.getTimestamp().getMillis())
						.build());
				
				return CaptureStoreReceipt.forSuccess(Instant.now(), capture);
			}
		});
	}


	@Override
	public void confirm(CaptureStoreReceipt<TData> receipt) {
		// TODO Auto-generated method stub
		
	}

}
