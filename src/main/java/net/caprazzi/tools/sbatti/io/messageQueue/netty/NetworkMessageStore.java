package net.caprazzi.tools.sbatti.io.messageQueue.netty;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.caprazzi.tools.sbatti.io.Capture;
import net.caprazzi.tools.sbatti.io.Capture.Captured;
import net.caprazzi.tools.sbatti.io.Capture.CapturedReceipt;
import net.caprazzi.tools.sbatti.io.netty.SimpleNettyProtobufClient;


public class NetworkMessageStore {
	
	public static void main(String[] args) throws UnknownHostException {
				
		SimpleNettyProtobufClient<Captured,CapturedReceipt> client 
			= new SimpleNettyProtobufClient<Capture.Captured, Capture.CapturedReceipt>
				(InetAddress.getLocalHost(), 3333, Capture.Captured.getDefaultInstance());
		
		client.start();
		
		
	}
	
}
