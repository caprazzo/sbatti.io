package net.caprazzi.tools.sbatti.io.netty;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.google.protobuf.MessageLite;

public class SimpleNettyProtobufClientPipelineFactory<TProto extends MessageLite> implements ChannelPipelineFactory {

	private TProto prototype;
	private ClientBootstrap bootstrap;
	private SimpleNettyReconnectStrategy strategy;
	private SimpleNettyProtobufClient client;

	public SimpleNettyProtobufClientPipelineFactory(
			ClientBootstrap bootstrap, 
			SimpleNettyReconnectStrategy strategy,
			SimpleNettyProtobufClient client, 
			TProto prototype) {
		this.bootstrap = bootstrap;
		this.strategy = strategy;
		this.client = client;
		this.prototype = prototype;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p = Channels.pipeline();
		
		p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		p.addLast("protobufDecoder", new ProtobufDecoder(prototype));
		
		p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
		p.addLast("protobufEncoder", new ProtobufEncoder());
		
		SimpleNettyProtobufClientHandler handler 
			= new SimpleNettyProtobufClientHandler(bootstrap, strategy.newInstance());
		client.setHandler(handler);
		
		p.addLast("handler", handler);
		return p;
	}

}
