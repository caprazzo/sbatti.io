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
