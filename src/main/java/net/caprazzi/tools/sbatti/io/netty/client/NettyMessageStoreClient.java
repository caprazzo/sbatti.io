package net.caprazzi.tools.sbatti.io.netty.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.caprazzi.tools.sbatti.io.Capture.Captured;
import net.caprazzi.tools.sbatti.io.Capture.CapturedReceipt;
import net.caprazzi.tools.sbatti.io.core.logging.Log;
import net.caprazzi.tools.sbatti.io.netty.SimpleNettyReconnectStrategy;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * Client to the NettyMessageStoreServer. 
 * This tries forever to reconnect to the given host and port.
 */
public class NettyMessageStoreClient {

	private static final Log log = Log.forClass(NettyMessageStoreClient.class);

	private InetSocketAddress address;

	private NettyMessageStoreClientHandler handler;

	public NettyMessageStoreClient(InetAddress host, int port) {		
		address = new InetSocketAddress(host, port);	
	}

	public void start() {
		log.info("Client connecting to {}", address);

		final ClientBootstrap bootstrap = new ClientBootstrap(
			new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));

		SimpleNettyReconnectStrategy strategy 
			= new SimpleNettyReconnectStrategy(5, TimeUnit.SECONDS);
		
		bootstrap.setPipelineFactory(
			new NettyMessageStoreClientPipelineFactory(
					bootstrap, 
					strategy,
					this));
			
		bootstrap.setOption("remoteAddress", address);		
		
		bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        
		bootstrap.connect(address);
	}
	
	public boolean isConnected() {
		return handler != null && handler.isConnected();
	}

	public CapturedReceipt send(Captured captured) {
		log.info("Sending " + captured);
		return handler.sendCapture(captured);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "/" + address;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public void setHandler(
			NettyMessageStoreClientHandler handler) {
				this.handler = handler;
		
	}

}
