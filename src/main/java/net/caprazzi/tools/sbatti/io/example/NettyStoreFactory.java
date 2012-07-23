package net.caprazzi.tools.sbatti.io.example;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.netty.NettyCaptureStore;
import net.caprazzi.tools.sbatti.io.netty.NettyCaptureStoreClient;

public class NettyStoreFactory {

	public static NettyCaptureStore<InterestingObject> newNettyStore(IDataSerializer<InterestingObject> serializer) {
				
		ListeningExecutorService executor 
			= MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
		
		NettyCaptureStoreClient client;
		try {
			client = new NettyCaptureStoreClient(InetAddress.getLocalHost(), 3333);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		
		client.start();
		
		return new NettyCaptureStore<InterestingObject>(executor, serializer, client);
		
	}

}
