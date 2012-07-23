package net.caprazzi.tools.sbatti.io.netty;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import net.caprazzi.tools.sbatti.io.Capture;
import net.caprazzi.tools.sbatti.io.Capture.Captured;
import net.caprazzi.tools.sbatti.io.Capture.CapturedReceipt;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.joda.time.Instant;

import com.google.common.base.Charsets;
import com.google.protobuf.ByteString;

public class NettyCaptureStoreClient {
	
	private InetAddress host;
	private int port;
	private NettyCaptureStoreClientHandler handler;
	
	public NettyCaptureStoreClient(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() {
		
		 ClientBootstrap bootstrap = new ClientBootstrap(
			 new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
		 
		 bootstrap.setPipelineFactory(new NettyCaptureStoreClientPipelineFactory());
		 
		 ChannelFuture connectFuture =
				 bootstrap.connect(new InetSocketAddress(host, port));
		 
		 Channel channel =
				connectFuture.awaitUninterruptibly().getChannel();
		 
		 handler = channel.getPipeline().get(NettyCaptureStoreClientHandler.class);		 
	}
	
	public static void main(String[] args) throws UnknownHostException, UnsupportedEncodingException {
		NettyCaptureStoreClient client 
			= new NettyCaptureStoreClient(InetAddress.getLocalHost(), 3333);
		
		client.start();
		
		for(;;) {
			CapturedReceipt receipt = client.send(Capture.Captured.newBuilder()
					.setId("zzzzzz")
					.setData(ByteString.copyFrom("payload", Charsets.UTF_8.toString()))
					.setSender("client")
					.setTimestamp(Instant.now().getMillis())
					.build());
			
			System.out.println("Receipt: " + receipt);
		}
	}

	private CapturedReceipt send(Captured captured) {
		System.out.println("Sending " + captured);
		return handler.sendCapture(captured);		
	}
	
	
	
}
