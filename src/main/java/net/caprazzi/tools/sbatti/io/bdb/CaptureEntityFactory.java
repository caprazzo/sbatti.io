package net.caprazzi.tools.sbatti.io.bdb;

import java.util.UUID;

import net.caprazzi.tools.sbatti.io.CapturedData;

import org.joda.time.Instant;

public interface CaptureEntityFactory<TData> {

	CapturedDataEntity forCapture(CapturedData<TData> capture);

	CapturedDataEntity forCapture(UUID id, byte[] data);

	CaptureDataTagEntity captureTag(String sender, UUID id, Instant timestamp);

	CaptureDataTagEntity storeTag(String string, UUID id, Instant now);
	
}
