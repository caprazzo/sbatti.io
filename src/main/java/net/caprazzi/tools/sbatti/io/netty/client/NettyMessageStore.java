package net.caprazzi.tools.sbatti.io.netty.client;

import java.util.concurrent.Callable;

import javax.naming.OperationNotSupportedException;

import net.caprazzi.tools.sbatti.io.Capture;
import net.caprazzi.tools.sbatti.io.Capture.CapturedReceipt;
import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.core.logging.Log;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessage;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageReceipt;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageStore;

import org.joda.time.Instant;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.protobuf.ByteString;

/**
 * An implementation of DataMessageStore that saves messages using NettyMessageStoreClient.
 */
public class NettyMessageStore<TData> implements DataMessageStore<TData> {
	
	private static final Log log = Log.forClass(NettyMessageStore.class);
	
	private final ListeningExecutorService executor;
	private final IDataSerializer<TData> serializer;
	private NettyMessageStoreClient client;
	
	/**
	 * Creates a new instance of NettyMessageStore
	 * @param executor - the executor where all the store() calls are executed
	 * @param serializer - serializer used to converd Message.data in byte[]
	 * @param client - an instance of NettyMessageStoreClient
	 */
	public NettyMessageStore(ListeningExecutorService executor, IDataSerializer<TData> serializer, NettyMessageStoreClient client) {
		this.executor = executor;
		this.serializer = serializer;
		this.client = client;		
	}			
	
	@Override
	public ListenableFuture<DataMessageReceipt> store(final String sender, final DataMessage<TData> capture) {
		
		log.debug("{}: Storing to {} (from {})", capture.getId(), client, sender);
		
		return executor.submit(new Callable<DataMessageReceipt>() {
			@Override
			public DataMessageReceipt call() throws Exception {
				
				if (!client.isConnected()) {
					log.warn("{}: Client is not connected", capture.getId());
					return DataMessageReceipt.forFailure(capture.getId(), Instant.now(), client + " is not connected");
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
					return DataMessageReceipt.forSuccess(capture.getId(), Instant.now(), "Delivered to " + send.getSender());
				else 
					return DataMessageReceipt.forFailure(capture.getId(), Instant.now(), "Failed delivery to " + send.getSender());
			}
		});
		
	}

	@Override
	public void confirm(DataMessageReceipt receipt) {
		throw new RuntimeException(new OperationNotSupportedException("This store does not support delivery confirmation"));		
	}
	
	@Override
	public String toString() {
		return "netty-store";
	}

}
