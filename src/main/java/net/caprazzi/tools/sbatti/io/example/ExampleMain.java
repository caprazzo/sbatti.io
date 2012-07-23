package net.caprazzi.tools.sbatti.io.example;

import java.io.File;

import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.QueueingDataProbe;
import net.caprazzi.tools.sbatti.io.QueueingDataProbeStoreConsumer;
import net.caprazzi.tools.sbatti.io.StoreCopyMarkStoreHandler;
import net.caprazzi.tools.sbatti.io.bdb.BdbCaptureEnvironment;
import net.caprazzi.tools.sbatti.io.bdb.BdbCaptureStore;
import net.caprazzi.tools.sbatti.io.bdb.BdbStoreFactory;
import net.caprazzi.tools.sbatti.io.netty.NettyCaptureStore;

public class ExampleMain {

	public static void main(String[] args) {
		
		// a probe that puts all capture data on a queue
		QueueingDataProbe<InterestingObject> probe 
			= new QueueingDataProbe<InterestingObject>();
		
		// this calls the probe many times 
		InterestingObjectGenerator generator 
			= new InterestingObjectGenerator(probe, 200000);
		
		
		
		// a simple logging store
		final LoggingCaptureStore<InterestingObject> storeTwo 
			= new LoggingCaptureStore<InterestingObject>("Logger Two");

		// create a serializer for your InterestingObject
		IDataSerializer<InterestingObject> serializer
			= new InterestingObjectDataSerizlier();
		
		NettyCaptureStore<InterestingObject> netStore 
			= NettyStoreFactory.newNettyStore(serializer);
		
		// create a berkeley db environment
		BdbCaptureEnvironment env 
			= new BdbCaptureEnvironment(new File("./example-bdb-db"));
				
		final BdbCaptureStore<InterestingObject> bdbStore 
			= BdbStoreFactory.newBdbStore(env, serializer);
		
		// join the two stores together:
		// - when write to the first store is confirmed,
		// - send the same data to the second store
		// - when write to the second store is confirmed,
		// - mark the data in the first store
		
		final StoreCopyMarkStoreHandler<InterestingObject> copyStore 
			= new StoreCopyMarkStoreHandler<InterestingObject>(bdbStore, netStore);
		
		QueueingDataProbeStoreConsumer<InterestingObject> consumer 
			= new QueueingDataProbeStoreConsumer<InterestingObject>(probe, copyStore);			

		consumer.start();
		generator.start();
	}
		

}
