package net.caprazzi.tools.sbatti.io.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.google.protobuf.MessageLite;

public class SimpleNettyProtobufServer
	<TReceive extends MessageLite, TSend extends MessageLite> {

	private int port;
	private TReceive prototype;
	
	public SimpleNettyProtobufServer(int port, TReceive prototype) {
		this.port = port;
		this.prototype = prototype;		
	}	

	public void start() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(
				new SimpleNettyProtobufServerPipelineFactory<TReceive, TSend>(prototype));

		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(port));
	}

}
