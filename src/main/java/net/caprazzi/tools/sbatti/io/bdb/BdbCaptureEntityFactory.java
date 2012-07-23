package net.caprazzi.tools.sbatti.io.bdb;

import java.util.UUID;

import net.caprazzi.tools.sbatti.io.CapturedData;
import net.caprazzi.tools.sbatti.io.example.InterestingObject;

import org.joda.time.Instant;

public class BdbCaptureEntityFactory implements
		CaptureEntityFactory<InterestingObject> {
	@Override
	public CapturedDataEntity forCapture(CapturedData<InterestingObject> capture) {
		return new CapturedDataEntity(
				capture.getId().toString(),
				capture.getData().getId());
	}

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
}
