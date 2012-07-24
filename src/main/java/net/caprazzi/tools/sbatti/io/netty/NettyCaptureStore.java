package net.caprazzi.tools.sbatti.io.netty;

import java.util.concurrent.Callable;

import javax.naming.OperationNotSupportedException;

import net.caprazzi.tools.sbatti.io.Capture;
import net.caprazzi.tools.sbatti.io.Capture.CapturedReceipt;
import net.caprazzi.tools.sbatti.io.CaptureStore;
import net.caprazzi.tools.sbatti.io.CaptureStoreReceipt;
import net.caprazzi.tools.sbatti.io.CapturedData;
import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.core.logging.Log;

import org.joda.time.Instant;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.protobuf.ByteString;

public class NettyCaptureStore<TData> implements CaptureStore<TData> {
	
	private static final Log log = Log.forClass(NettyCaptureStore.class);
	
	private final ListeningExecutorService executor;
	private final IDataSerializer<TData> serializer;
	private NettyCaptureStoreClient client;
	
	public NettyCaptureStore(ListeningExecutorService executor, IDataSerializer<TData> serializer, NettyCaptureStoreClient client) {
		this.executor = executor;
		this.serializer = serializer;
		this.client = client;		
	}			
	
	@Override
	public ListenableFuture<CaptureStoreReceipt> store(final String sender, final CapturedData<TData> capture) {
		
		log.debug("{}: Storing to {} (from {})", capture.getId(), client, sender);
		
		return executor.submit(new Callable<CaptureStoreReceipt>() {
			@Override
			public CaptureStoreReceipt call() throws Exception {
				
				if (!client.isConnected()) {
					log.warn("{}: Client is not connected", capture.getId());
					return CaptureStoreReceipt.forFailure(capture.getId(), Instant.now(), client + " is not connected");
				}
				
				log.info("{}: Sending to client {}", capture.getId(), client);
				
				CapturedReceipt send = client.send(Capture.Captured
					.newBuilder()
					.setData(ByteString.copyFrom(serializer.serialize(capture.getData())))
					.setId(capture.getId().toString())
					.setSender(sender)
					.setTimestamp(capture.getTimestamp().getMillis())
					.build());
				
				log.info("Sent to server: {} ", send);
				
				if (send.hasSuccess())					
					return CaptureStoreReceipt.forSuccess(capture.getId(), Instant.now(), "Delivered to " + send.getSender());
				else 
					return CaptureStoreReceipt.forFailure(capture.getId(), Instant.now(), "Failed delivery to " + send.getSender());
			}
		});
		
	}

	@Override
	public void confirm(CaptureStoreReceipt receipt) {
		throw new RuntimeException(new OperationNotSupportedException("This store does not support delivery confirmation"));		
	}
	
	@Override
	public String toString() {
		return "netty-store";
	}

}
