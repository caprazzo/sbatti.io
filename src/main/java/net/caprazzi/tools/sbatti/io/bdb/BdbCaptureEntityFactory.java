package net.caprazzi.tools.sbatti.io.bdb;

import java.util.UUID;

import org.joda.time.Instant;

//TODO: move to general implementation
public class BdbCaptureEntityFactory implements CaptureEntityFactory {
	
	@Override
	public CapturedDataEntity forCapture(UUID id, byte[] data) {
		return new CapturedDataEntity(
			id.toString(),
			CaptureStatus.UNDELIVERED,
			Instant.now().getMillis(),
			data);
	}

	@Override
	public CapturedDataEntity forConfirmed(UUID captureId) {
		return new CapturedDataEntity(
			captureId.toString(),
			CaptureStatus.DELIVERED,
			Instant.now().getMillis(),
			null);
	}


}
