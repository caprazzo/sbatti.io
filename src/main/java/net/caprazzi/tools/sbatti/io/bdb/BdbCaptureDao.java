package net.caprazzi.tools.sbatti.io.bdb;

import java.util.UUID;

import net.caprazzi.tools.sbatti.io.core.BdbRetryDataProbe;
import net.caprazzi.tools.sbatti.io.core.logging.Log;
import net.caprazzi.tools.sbatti.io.netty.client.NettyMessageStore;

import org.joda.time.Instant;

import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class BdbCaptureDao<TData> {
		
	private static final Log log = Log.forClass(BdbCaptureDao.class);
	
	private final EntityStore entityStore;
	private final CaptureEntityFactory factory;
	
	private final PrimaryIndex<String, CapturedDataEntity> captureDataPx;
	private SecondaryIndex<CaptureStatus, String, CapturedDataEntity> captureStatusPx;

	public BdbCaptureDao(BdbCaptureEnvironment env, CaptureEntityFactory factory) {
		this.entityStore = env.getEntityStore();
		this.factory = factory;
		captureDataPx = entityStore.getPrimaryIndex(String.class, CapturedDataEntity.class);
		captureStatusPx = entityStore.getSecondaryIndex(captureDataPx, CaptureStatus.class, "status");
	}		
	
	public void save(String sender, UUID id, Instant timestamp, byte[] data) {
		CapturedDataEntity entity = factory.forCapture(id, data);
		Transaction txn = entityStore.getEnvironment().beginTransaction(null, null);
		try {
			captureDataPx.put(entity);
			txn.commit();
		}
		catch(Exception e) {
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}
	}
	
	public void confirm(UUID captureId) {
		Transaction txn = entityStore.getEnvironment().beginTransaction(null, null);
		try {
			CapturedDataEntity found = captureDataPx.get(captureId.toString());
			if (found != null) {
				found.setStatus(CaptureStatus.DELIVERED);
				captureDataPx.put(found);
			}
			txn.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}		
	}
	
	public void findUndelivered(BdbRetryDataProbe<TData> probe) {
		
		EntityIndex<String, CapturedDataEntity> subIndex = captureStatusPx.subIndex(CaptureStatus.UNDELIVERED);
		
		EntityCursor<CapturedDataEntity> entities = subIndex.entities();				
		try {
			for (CapturedDataEntity e : entities) {
				System.out.println("Found undelivered message: " + e.getCaptureId());
				probe.capture(e);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			entities.close();
		}
		
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	
	
}
