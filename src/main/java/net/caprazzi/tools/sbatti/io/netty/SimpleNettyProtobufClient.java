package net.caprazzi.tools.sbatti.io.netty;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.caprazzi.tools.sbatti.io.Capture;
import net.caprazzi.tools.sbatti.io.core.logging.Log;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.google.protobuf.MessageLite;

public class SimpleNettyProtobufClient<TProto extends MessageLite> {

	private static final Log log 
		= Log.forClass(SimpleNettyProtobufClient.class);
	
	private InetSocketAddress address;

	private SimpleNettyReconnectStrategy strategy;

	private TProto prototype;
	
	public SimpleNettyProtobufClient(InetAddress host, int port, TProto prototype) {
		this.prototype = prototype;
		address = new InetSocketAddress(host, port);
	}
	
	public void start() {
		log.info("Client connecting to {}", address);

		final ClientBootstrap bootstrap = new ClientBootstrap(
			new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));

		strategy = new SimpleNettyReconnectStrategy(5, TimeUnit.SECONDS);
		
		bootstrap.setPipelineFactory(
			new SimpleNettyProtobufClientPipelineFactory<TProto>(
					bootstrap, 
					strategy,
					this,
					prototype));
			
		bootstrap.setOption("remoteAddress", address);		
		
		bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        
		bootstrap.connect(address);
	}

	public void setHandler(SimpleNettyProtobufClientHandler handler) {
		// TODO Auto-generated method stub
		
	}
	
	
}
