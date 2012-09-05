package net.caprazzi.tools.sbatti.io.example;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.QueueingDataProbe;
import net.caprazzi.tools.sbatti.io.QueueingDataProbeStoreConsumer;
import net.caprazzi.tools.sbatti.io.StoreCopyMarkStoreHandler;
import net.caprazzi.tools.sbatti.io.StoreEventListener;
import net.caprazzi.tools.sbatti.io.bdb.BdbCaptureEnvironment;
import net.caprazzi.tools.sbatti.io.bdb.BdbCaptureStore;
import net.caprazzi.tools.sbatti.io.bdb.BdbStoreFactory;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessageReceipt;
import net.caprazzi.tools.sbatti.io.netty.client.NettyMessageStore;

public class ExampleMain {

	public static void main(String[] args) throws UnknownHostException {
		
		// a probe that puts all capture data on a queue
		QueueingDataProbe<InterestingObject> probe 
			= new QueueingDataProbe<InterestingObject>("example-probe");
		
		// this calls the probe on a configurable number of
		// interesting objects
		InterestingObjectGenerator generator 
			= new InterestingObjectGenerator(probe, 200000);

		// create a serializes for your InterestingObject
		IDataSerializer<InterestingObject> serializer
			= new InterestingObjectDataSerizlier();
		
		// probe used to retry undelivered messages
		QueueingDataProbe<InterestingObject> recovery 
			= new QueueingDataProbe<InterestingObject>("recovery-probe");
		
		
		
		// create a berkeley db environment
		BdbCaptureEnvironment env 
			= new BdbCaptureEnvironment(new File("./example-bdb-db"));
		
		// create the local on-file store
		final BdbCaptureStore<InterestingObject> bdbStore 
			= BdbStoreFactory.newBdbStore(env, serializer, recovery);		
		
		// create a remote network based store
		NettyMessageStore<InterestingObject> netStore 
			= NettyStoreFactory.newNettyStore(
					InetAddress.getLocalHost(), 3333,
					serializer);
		
		// a probe that puts all capture data on a queue
		QueueingDataProbeStoreConsumer<InterestingObject> recoveryConsumer
			= new QueueingDataProbeStoreConsumer<InterestingObject>(recovery, netStore);
		
		recoveryConsumer.addListener(new StoreEventListener() {
			
			@Override
			public void onComplete(DataMessageReceipt receipt) {
				if (receipt.isSuccess()) {
					bdbStore.confirm(receipt);
				}
				else {
					
				}
			}
		});
		
		// the store-copy-mark store stores a capture locally
		// and then remotely. it removes the local copy when the
		// remote store returns success
		final StoreCopyMarkStoreHandler<InterestingObject> copyStore 
			= new StoreCopyMarkStoreHandler<InterestingObject>(bdbStore, netStore);
		
		// get data from the capture probe
		// and sends it to the copy store
		QueueingDataProbeStoreConsumer<InterestingObject> consumer 
			= new QueueingDataProbeStoreConsumer<InterestingObject>(probe, copyStore);			

		
		recoveryConsumer.start();
		
		// start the consumer
		consumer.start();
		
		// start the generator
		generator.start();
		
		
	}
		

}

