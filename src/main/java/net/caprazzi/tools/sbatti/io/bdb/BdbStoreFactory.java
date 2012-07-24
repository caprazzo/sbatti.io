package net.caprazzi.tools.sbatti.io.bdb;

import java.util.concurrent.Executors;

import net.caprazzi.tools.sbatti.io.IDataProbe;
import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.core.BdbRetryDataProbe;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class BdbStoreFactory {

	public static <TData> BdbCaptureStore<TData> newBdbStore(BdbCaptureEnvironment env, IDataSerializer<TData> serializer, IDataProbe<TData> undeliveredProbe) {
		
		CaptureEntityFactory factory 
			= new BdbCaptureEntityFactory();
		
		BdbCaptureDao<TData> dao 
			= new BdbCaptureDao<TData>(env, factory);
		
		ListeningExecutorService executor 
		= MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
	
		final BdbCaptureStore<TData> bdbStore 
			= new BdbCaptureStore<TData>(executor, dao, serializer);
		
		BdbRetryDataProbe<TData> bdbRetryProbe 
			= new BdbRetryDataProbe<TData>(serializer, undeliveredProbe);
		
		BdbUndeliveredScanner<TData> scanner
			= new BdbUndeliveredScanner<TData>(dao, bdbRetryProbe);
		
		scanner.start();
		
		return bdbStore;	
	}
	
}
