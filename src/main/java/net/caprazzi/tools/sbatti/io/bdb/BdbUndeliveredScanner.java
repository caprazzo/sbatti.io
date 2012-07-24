package net.caprazzi.tools.sbatti.io.bdb;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.caprazzi.tools.sbatti.io.core.BdbRetryDataProbe;
import net.caprazzi.tools.sbatti.io.core.logging.Log;

public class BdbUndeliveredScanner<TData> {

	private static final Log log = Log.forClass(BdbUndeliveredScanner.class);
	
	private BdbCaptureDao<TData> dao;
	private BdbRetryDataProbe<TData> undeliveredProbe;

	public BdbUndeliveredScanner(BdbCaptureDao<TData> dao) {
		this.dao = dao;
	}
	
	public BdbUndeliveredScanner(BdbCaptureDao<TData> dao,
			BdbRetryDataProbe<TData> undeliveredProbe) {
				this.dao = dao;
				this.undeliveredProbe = undeliveredProbe;
	}

	public void findUndelivered() {
		dao.findUndelivered(undeliveredProbe);
	}
	
	public void start() {
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
		
		
		pool.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				log.info("Scanning for undelivered messages");
				findUndelivered();
			}
		}, 5, 2, TimeUnit.SECONDS);
	}
	
}
