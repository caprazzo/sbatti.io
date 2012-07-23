package net.caprazzi.tools.sbatti.io.netty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.caprazzi.tools.sbatti.io.Capture.Captured;
import net.caprazzi.tools.sbatti.io.Capture.CapturedReceipt;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class NettyCaptureStoreClientHandler extends SimpleChannelUpstreamHandler {

	private static final Logger Log = Logger
			.getLogger(NettyCaptureStoreClientHandler.class.getName());

	private volatile Channel channel;

	// TODO: should be capturedReceipt
	private final BlockingQueue<CapturedReceipt> answer = new LinkedBlockingQueue<CapturedReceipt>();

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
			Log.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		channel = e.getChannel();
		super.channelOpen(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) {
		boolean offered = answer.offer((CapturedReceipt) e.getMessage());
		assert offered;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Log.log(Level.WARNING, "Unexpected exception from downstream.",
				e.getCause());
		e.getChannel().close();
	}

}
