package net.caprazzi.tools.sbatti.io.example;

import java.net.InetAddress;
import java.util.concurrent.Executors;

import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.netty.client.NettyMessageStore;
import net.caprazzi.tools.sbatti.io.netty.client.NettyMessageStoreClient;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class NettyStoreFactory {

	public static NettyMessageStore<InterestingObject> newNettyStore(InetAddress host, int port, IDataSerializer<InterestingObject> serializer) {
				
		ListeningExecutorService executor 
			= MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
		
		NettyMessageStoreClient client 
			= new NettyMessageStoreClient(host, port);
		
		client.start();
		
		return new NettyMessageStore<InterestingObject>(executor, serializer, client);
		
	}

}
