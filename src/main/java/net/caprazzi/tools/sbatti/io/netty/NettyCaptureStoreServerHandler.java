package net.caprazzi.tools.sbatti.io.netty;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.caprazzi.tools.sbatti.io.Capture.Captured;
import net.caprazzi.tools.sbatti.io.Capture.CapturedReceipt;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.joda.time.Instant;

public class NettyCaptureStoreServerHandler extends SimpleChannelUpstreamHandler {

	private static final Logger Log = Logger
			.getLogger(NettyCaptureStoreClientHandler.class.getName());
	
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
			Log.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		
		Captured captured = (Captured) e.getMessage();
		
		e.getChannel().write(CapturedReceipt.newBuilder()
				.setId(captured.getId())
				.setTimestamp(Instant.now().getMillis())
				.setSender("netty-server")
				.setSuccess(true));		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Log.log(
			Level.WARNING, 
			"Unexpected exception from downstream.",
			e.getCause());
		e.getChannel().close();
	}
	
}
