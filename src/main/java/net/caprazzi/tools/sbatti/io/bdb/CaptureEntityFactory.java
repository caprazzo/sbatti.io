package net.caprazzi.tools.sbatti.io.bdb;

import java.util.UUID;

import org.joda.time.Instant;

public interface CaptureEntityFactory {

	CapturedDataEntity forCapture(UUID id, byte[] data);

	CaptureDataTagEntity captureTag(String sender, UUID id, Instant timestamp);

	CaptureDataTagEntity storeTag(String string, UUID id, Instant now);

	CaptureDataTagEntity confirmTag(String sender, UUID id, Instant timestamp);
	
}
