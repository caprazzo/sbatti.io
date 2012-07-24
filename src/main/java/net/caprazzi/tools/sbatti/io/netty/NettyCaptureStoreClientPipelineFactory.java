package net.caprazzi.tools.sbatti.io.netty;

import net.caprazzi.tools.sbatti.io.Capture;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

public class NettyCaptureStoreClientPipelineFactory implements ChannelPipelineFactory {

	private ClientBootstrap bootstrap;
	private NettyCaptureStoreReconnectStrategy strategy;
	private NettyCaptureStoreClient client;

	public NettyCaptureStoreClientPipelineFactory(ClientBootstrap bootstrap, NettyCaptureStoreReconnectStrategy strategy, NettyCaptureStoreClient nettyCaptureStoreClient) {
		this.bootstrap = bootstrap;
		this.strategy = strategy;
		this.client = nettyCaptureStoreClient;		
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p = Channels.pipeline();
		
		p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		p.addLast("protobufDecoder", new ProtobufDecoder(Capture.CapturedReceipt.getDefaultInstance()));
		
		p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
		p.addLast("protobufEncoder", new ProtobufEncoder());
		
		p.addLast("handler", new NettyCaptureStoreClientHandler(bootstrap, strategy.newInstance(), client));
		
		return p;
	}

}
