package net.caprazzi.tools.sbatti.io.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class NettyCaptureStoreServer {

	private int port;
	
	public NettyCaptureStoreServer(int port) {
		this.port = port;		
	}	

	public void start() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new NettyCaptureStoreServerPipelineFactory());

		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(port));
	}
	
	public static void main(String[] args) {
		NettyCaptureStoreServer server = new NettyCaptureStoreServer(3333);
		server.start();
	}

}
