package net.caprazzi.tools.sbatti.io.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import net.caprazzi.tools.sbatti.io.core.logging.Log;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;

public class SimpleNettyReconnectStrategy {

	private static final Log log = Log
			.forClass(SimpleNettyReconnectStrategy.class);
	
	private Timer timer = new HashedWheelTimer();
	private int reconnectDelay;
	private TimeUnit reconnectUnit;

	InetSocketAddress getRemoteAddress(ClientBootstrap bootstrap) {
		return (InetSocketAddress) bootstrap.getOption("remoteAddress");
	}
	
	public SimpleNettyReconnectStrategy(int reconnectDelay, TimeUnit reconnectUnit) {
		this.reconnectUnit = reconnectUnit;
		this.reconnectDelay = reconnectDelay;
	}

	public SimpleNettyReconnectStrategy newInstance() {
		return new SimpleNettyReconnectStrategy(reconnectDelay, reconnectUnit);
	}

	public void handleChannelClosed(final ClientBootstrap bootstrap) {
		timer.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				log.info("Reconnecting to: {}" + getRemoteAddress(bootstrap));
				bootstrap.connect();
			}
			
		}, reconnectDelay, reconnectUnit);		
	}

}
