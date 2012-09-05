package net.caprazzi.tools.sbatti.io.netty.client;

import net.caprazzi.tools.sbatti.io.Capture;
import net.caprazzi.tools.sbatti.io.netty.SimpleNettyReconnectStrategy;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class NettyMessageStoreClientPipelineFactory implements ChannelPipelineFactory {

	private ClientBootstrap bootstrap;
	private SimpleNettyReconnectStrategy strategy;
	private NettyMessageStoreClient client;

	public NettyMessageStoreClientPipelineFactory(ClientBootstrap bootstrap, SimpleNettyReconnectStrategy strategy, NettyMessageStoreClient nettyCaptureStoreClient) {
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
		
		NettyMessageStoreClientHandler handler 
			= new NettyMessageStoreClientHandler(bootstrap, strategy.newInstance());
		
		client.setHandler(handler);
		p.addLast("handler", handler);
		
		return p;
	}

}
