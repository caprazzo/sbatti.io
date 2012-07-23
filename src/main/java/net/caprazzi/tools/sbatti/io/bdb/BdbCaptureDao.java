package net.caprazzi.tools.sbatti.io.bdb;

import java.util.UUID;

import org.joda.time.Instant;

import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

public class BdbCaptureDao<TData> {
		
	private final EntityStore entityStore;
	private final CaptureEntityFactory factory;
	
	private final PrimaryIndex<String, CapturedDataEntity> captureDataPx;
	private final PrimaryIndex<String, CaptureDataTagEntity> captureTagPx;

	public BdbCaptureDao(BdbCaptureEnvironment env, CaptureEntityFactory factory) {
		this.entityStore = env.getEntityStore();
		this.factory = factory;
		captureDataPx = entityStore.getPrimaryIndex(String.class, CapturedDataEntity.class);
		captureTagPx = entityStore.getPrimaryIndex(String.class, CaptureDataTagEntity.class);
	}		
	
	public void save(String sender, UUID id, Instant timestamp, byte[] data) {
		CapturedDataEntity entity = factory.forCapture(id, data);
		CaptureDataTagEntity captureTag = factory.captureTag(sender.toString(), id, timestamp);
		CaptureDataTagEntity storeTag = factory.storeTag("bdb-capture-dao", id, Instant.now());
	
		captureDataPx.put(entity);
		captureTagPx.put(captureTag);
		captureTagPx.put(storeTag);
	}

	public void markConfirmed(String sender, Instant timestamp, UUID id) {
		CaptureDataTagEntity confirmTag = factory.confirmTag(sender, id, timestamp);		
		captureTagPx.put(confirmTag);
	}
	
}
