package net.caprazzi.tools.sbatti.io.netty.client;

import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.caprazzi.tools.sbatti.io.Capture.Captured;
import net.caprazzi.tools.sbatti.io.Capture.CapturedReceipt;
import net.caprazzi.tools.sbatti.io.core.logging.Log;
import net.caprazzi.tools.sbatti.io.netty.SimpleNettyReconnectStrategy;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class NettyMessageStoreClientHandler extends
		SimpleChannelUpstreamHandler {

	private static final Log log = Log
			.forClass(NettyMessageStoreClientHandler.class);

	private volatile Channel channel;

	private final BlockingQueue<CapturedReceipt> answer 
		= new LinkedBlockingQueue<CapturedReceipt>();

	private ClientBootstrap bootstrap;

	private long startTime = -1;

	private SimpleNettyReconnectStrategy strategy;

	public NettyMessageStoreClientHandler(ClientBootstrap bootstrap, SimpleNettyReconnectStrategy strategy) {
		this.bootstrap = bootstrap;
		this.strategy = strategy;
	}

	InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) bootstrap.getOption("remoteAddress");
	}

	public CapturedReceipt sendCapture(Captured captured) {
		channel.write(captured);
		CapturedReceipt receipt;
		boolean interrupted = false;
		for (;;) {
			try {
				receipt = answer.take();
				break;
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}

		if (interrupted) {
			Thread.currentThread().interrupt();
		}
		return receipt;
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
			log.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		
		channel = e.getChannel();
		log.info("Channel open:{}", channel);
		super.channelOpen(ctx, e);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		log.warn("Disconnected from: " + getRemoteAddress());
		e.getChannel().close();
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		if (startTime < 0) {
			startTime = System.currentTimeMillis();
		}
		log.info("Connected to: " + getRemoteAddress());
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		log.info("Disconnected");		
		strategy.handleChannelClosed(bootstrap);				
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) {
		boolean offered = answer.offer((CapturedReceipt) e.getMessage());
		assert offered;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		log.warn("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}

	public boolean isConnected() {
		return channel != null && channel.isConnected();
	}

}
