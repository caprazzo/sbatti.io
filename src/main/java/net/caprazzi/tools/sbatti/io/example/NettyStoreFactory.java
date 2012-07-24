package net.caprazzi.tools.sbatti.io.example;

import java.net.InetAddress;
import java.util.concurrent.Executors;

import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.netty.NettyCaptureStore;
import net.caprazzi.tools.sbatti.io.netty.NettyCaptureStoreClient;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class NettyStoreFactory {

	public static NettyCaptureStore<InterestingObject> newNettyStore(InetAddress host, int port, IDataSerializer<InterestingObject> serializer) {
				
		ListeningExecutorService executor 
			= MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
		
		NettyCaptureStoreClient client 
			= new NettyCaptureStoreClient(host, port);
		
		client.start();
		
		return new NettyCaptureStore<InterestingObject>(executor, serializer, client);
		
	}

}
