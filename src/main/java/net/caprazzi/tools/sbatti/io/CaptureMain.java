package net.caprazzi.tools.sbatti.io;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import net.caprazzi.tools.sbatti.io.bdb.BdbCaptureDao;
import net.caprazzi.tools.sbatti.io.bdb.BdbCaptureEntityFactory;
import net.caprazzi.tools.sbatti.io.bdb.BdbCaptureStore;
import net.caprazzi.tools.sbatti.io.bdb.CaptureEntityFactory;
import net.caprazzi.tools.sbatti.io.example.InterestingObject;
import net.caprazzi.tools.sbatti.io.example.InterestingObjectDataSerizlier;
import net.caprazzi.tools.sbatti.io.example.InterestingObjectGenerator;
import net.caprazzi.tools.sbatti.io.example.LoggingCaptureStore;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

public class CaptureMain {

	public static void main(String[] args) {
				
		// the queue where the probe will put all captured data
		final BlockingQueue<CapturedData<InterestingObject>> queue
			= new LinkedBlockingQueue<CapturedData<InterestingObject>>();
		
		// the probe that does the actual capture
		IDataProbe<InterestingObject> probe 
			= new QueueingDataProbe<InterestingObject>(queue);
		
		// this calls the probe many times 
		InterestingObjectGenerator generator 
			= new InterestingObjectGenerator(probe, 2);
		
		// a simple logging store
		final LoggingCaptureStore<InterestingObject> storeTwo 
			= new LoggingCaptureStore<InterestingObject>("Logger Two");
		
		EntityStore entityStore = getBdbStore();		
		CaptureEntityFactory<InterestingObject> factory = new BdbCaptureEntityFactory();
		BdbCaptureDao<InterestingObject> dao = new BdbCaptureDao<InterestingObject>(entityStore, factory);
		
		ListeningExecutorService executor 
			= MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
		
		IDataSerializer<InterestingObject> serializer
			= new InterestingObjectDataSerizlier();
		
		final BdbCaptureStore<InterestingObject> bdbStore 
			= new BdbCaptureStore<InterestingObject>(executor, dao, serializer);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						
						// take a message from queue
						CapturedData<InterestingObject> capture = queue.take();
						
						System.out.println("Dequeued " + capture);
						
						// request to store in local store
						ListenableFuture<CaptureStoreReceipt<InterestingObject>> stored 
							= bdbStore.store("main-deque-loop", capture);
						
						// listen for store response
						Futures.addCallback(stored, new FutureCallback<CaptureStoreReceipt<InterestingObject>>() {

							@Override
							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub								
							}

							@Override
							public void onSuccess(CaptureStoreReceipt<InterestingObject> receipt) {		
								
								// on success, store in second store
								ListenableFuture<CaptureStoreReceipt<InterestingObject>> storedTwo 
									= storeTwo.store("logger-on-confirm", receipt.getCapture());
								
								Futures.addCallback(storedTwo, 
									new FutureCallback<CaptureStoreReceipt<InterestingObject>>() {

										@Override
										public void onFailure(Throwable arg0) {
											// TODO Auto-generated method stub												
										}

										@Override
										public void onSuccess(CaptureStoreReceipt<InterestingObject> receipt) {
											// on success, remove from local store
											bdbStore.delete(receipt);
										}});
							}
						});
						
						
						// file store 
						// -> confirm 
						// -> network store 
						// -> confirm 
						// -> file store delete
						
						// network store
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}			
			}			
		}).start();
		
		
		generator.start();		
		
	}

	private static EntityStore getBdbStore() {
		EnvironmentConfig myEnvConfig = new EnvironmentConfig();
	    StoreConfig storeConfig = new StoreConfig();

	    myEnvConfig.setAllowCreate(true);
	    storeConfig.setAllowCreate(true);

	    File home = new File("./bdb-home");
		// Open the environment and entity store
		Environment myEnv = new Environment(home, myEnvConfig);
		return new EntityStore(myEnv, "EntityStore", storeConfig);
	}
	
}
