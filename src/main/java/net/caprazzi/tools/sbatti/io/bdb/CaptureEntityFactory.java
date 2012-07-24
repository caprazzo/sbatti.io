package net.caprazzi.tools.sbatti.io.bdb;

import java.util.UUID;

public interface CaptureEntityFactory {

	CapturedDataEntity forCapture(UUID id, byte[] data);

	CapturedDataEntity forConfirmed(UUID captureId);
	
}
