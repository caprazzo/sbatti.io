package net.caprazzi.tools.sbatti.io.bdb;

import java.util.concurrent.Executors;

import net.caprazzi.tools.sbatti.io.IDataSerializer;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class BdbStoreFactory {

	public static <TData> BdbCaptureStore<TData> newBdbStore(BdbCaptureEnvironment env, IDataSerializer<TData> serializer) {
		
		CaptureEntityFactory factory 
			= new BdbCaptureEntityFactory();
		
		BdbCaptureDao<TData> dao 
			= new BdbCaptureDao<TData>(env, factory);
		
		ListeningExecutorService executor 
		= MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
	
		final BdbCaptureStore<TData> bdbStore 
			= new BdbCaptureStore<TData>(executor, dao, serializer);
		
		return bdbStore;	
	}
	
}
