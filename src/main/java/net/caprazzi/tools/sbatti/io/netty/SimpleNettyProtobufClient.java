package net.caprazzi.tools.sbatti.io.netty;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.caprazzi.tools.sbatti.io.core.logging.Log;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.google.common.base.Optional;
import com.google.protobuf.MessageLite;

public class SimpleNettyProtobufClient<TSend extends MessageLite, TReceive extends MessageLite> {

	private static final Log log 
		= Log.forClass(SimpleNettyProtobufClient.class);
	
	private InetSocketAddress address;

	private SimpleNettyReconnectStrategy strategy = 
				new SimpleNettyReconnectStrategy(5, TimeUnit.SECONDS);

	private TSend prototype;

	private SimpleNettyProtobufClientHandler<TSend, TReceive> handler;
	
	public SimpleNettyProtobufClient(InetAddress host, int port, TSend sendPrototype) {
		this.prototype = sendPrototype;
		this.address = new InetSocketAddress(host, port);
	}
	
	public void start() {
		log.info("Client connecting to {}", address);

		final ClientBootstrap bootstrap 
			= new ClientBootstrap(getChannelFactory());

		bootstrap.setPipelineFactory(getPipelineFactory(bootstrap));
			
		bootstrap.setOption("remoteAddress", address);		
		
		bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);       
        
		bootstrap.connect(address);
	}

	private NioClientSocketChannelFactory getChannelFactory() {
		return new NioClientSocketChannelFactory(
			Executors.newCachedThreadPool(),
			Executors.newCachedThreadPool());
	}

	private ChannelPipelineFactory getPipelineFactory(ClientBootstrap bootstrap) {
		return new SimpleNettyProtobufClientPipelineFactory<TSend, TReceive>(
				bootstrap, 
				strategy,
				this,
				prototype);
	}
	
	public Optional<TReceive> send(TSend data) {
		if (!isConnected()) {
			// TODO: throw
			return Optional.absent();
		}
		return Optional.fromNullable(handler.send(data));
	}

	public void setHandler(SimpleNettyProtobufClientHandler<TSend, TReceive> handler) {
		this.handler = handler;		
	}
	
	public boolean isConnected() {
		return handler != null && handler.isConnected();
	}
	
	
}
