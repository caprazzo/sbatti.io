package learn;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.google.common.base.Charsets;

public class LearnNettyServer {

	public static void main(String[] args) {
	
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				EchoServer.start();
				
			}}).start();
		
		
		EchoClient.start();
		
	}
	
	public static class EchoClient {
		
		public static void start() {
			
			ChannelFactory factory 
				= new NioClientSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool());
			
			ClientBootstrap bootstrap 
				= new ClientBootstrap(factory);
			
			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
				
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return Channels.pipeline(new EchoClientHandler());
				}
			});
			
			bootstrap.setOption("tcpNoDelay", true);
			bootstrap.setOption("keepAlive", true);
			
			bootstrap.connect(new InetSocketAddress(3333));							
		}
		
	}
	
	private static class EchoClientHandler extends SimpleChannelHandler {
		
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
			ChannelBuffer buf = (ChannelBuffer)e.getMessage();
			System.out.println(buf.toString());
		}				
		
		@Override
		public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
			while (true) {
				e.getChannel().write(ChannelBuffers.copiedBuffer("hello dude", Charsets.UTF_8));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		@Override
		 public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
           e.getCause().printStackTrace();
           Channel ch = e.getChannel();
           ch.close();
       }
		
	}
 
	public static class EchoServerHandler extends SimpleChannelHandler {
		
		
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			Channel ch = e.getChannel();
			 ChannelBuffer buf = (ChannelBuffer) e.getMessage();
			 while(buf.readable()) {
	           System.out.println((char) buf.readByte());
	           System.out.flush();
			}
			ch.write(buf);
		}
		
		@Override
		 public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
            e.getCause().printStackTrace();
            Channel ch = e.getChannel();
            ch.close();
        }
		
	}
	
	public static class EchoServer {
		
		public static void start() {
			NioServerSocketChannelFactory factory 
				= new NioServerSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool());
			
			ServerBootstrap bootstrap 
				= new ServerBootstrap(factory);
			
			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
				
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return Channels.pipeline(new EchoServerHandler());
				}
			});
			
			bootstrap.setOption("child.tcpNoDelay", true);
			bootstrap.setOption("child.keepAlive", true);
			
			bootstrap.bind(new InetSocketAddress(3333));
		}
		
	}
	
}
