package net.caprazzi.tools.sbatti.io.bdb;

import java.util.UUID;

import net.caprazzi.tools.sbatti.io.CapturedData;

import org.joda.time.Instant;

import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

public class BdbCaptureDao<TData> {
		
	private final EntityStore store;
	private final CaptureEntityFactory<TData> factory;
	
	private final PrimaryIndex<String, CapturedDataEntity> captureDataPx;
	private final PrimaryIndex<String, CaptureDataTagEntity> captureTagPx;

	public BdbCaptureDao(EntityStore store, CaptureEntityFactory<TData> factory) {
		this.store = store;
		this.factory = factory;
		captureDataPx = store.getPrimaryIndex(String.class, CapturedDataEntity.class);
		captureTagPx = store.getPrimaryIndex(String.class, CaptureDataTagEntity.class);
	}
	
	public void save(CapturedData<TData> capture) {
		CapturedDataEntity entity = factory.forCapture(capture);
		captureDataPx.put(entity);
		System.out.println("Bdb capture store saved capture " + capture);
	}

	public void save(String sender, UUID id, Instant timestamp, byte[] data) {
		CapturedDataEntity entity = factory.forCapture(id, data);
		CaptureDataTagEntity captureTag = factory.captureTag(sender.toString(), id, timestamp);
		CaptureDataTagEntity storeTag = factory.storeTag("bdb-capture-dao", id, Instant.now());
	
		captureDataPx.put(entity);
		captureTagPx.put(captureTag);
		captureTagPx.put(storeTag);
	}
	
}
