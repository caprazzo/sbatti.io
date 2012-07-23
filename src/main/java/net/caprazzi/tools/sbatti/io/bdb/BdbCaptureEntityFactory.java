package net.caprazzi.tools.sbatti.io.bdb;

import java.util.UUID;

import org.joda.time.Instant;

//TODO: move to general implementation
public class BdbCaptureEntityFactory implements CaptureEntityFactory {
	
	@Override
	public CapturedDataEntity forCapture(UUID id, byte[] data) {
		return new CapturedDataEntity(
			id.toString(),
			data);
	}

	@Override
	public CaptureDataTagEntity captureTag(String sender, UUID id, Instant timestamp) {
		return new CaptureDataTagEntity(
			id.toString(),
			timestamp.getMillis(),
			"captured-by",
			sender);
	}

	@Override
	public CaptureDataTagEntity storeTag(String sender, UUID id, Instant timestamp) {
		return new CaptureDataTagEntity(
			id.toString(),
			timestamp.getMillis(),
			"stored-by",
			sender);
	}

	@Override
	public CaptureDataTagEntity confirmTag(String sender, UUID id, Instant timestamp) {
		return new CaptureDataTagEntity(
			id.toString(),
			timestamp.getMillis(),
			"copy-confirmed",
			sender);
	}
}
