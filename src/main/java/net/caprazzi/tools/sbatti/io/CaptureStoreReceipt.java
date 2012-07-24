package net.caprazzi.tools.sbatti.io;

import java.util.UUID;

import org.joda.time.Instant;

public class CaptureStoreReceipt {

	private final boolean success;
	private final Instant timestamp;
	private final UUID captureId;
	private final String message;
	private final Throwable cause;

	private CaptureStoreReceipt(UUID captureId, Instant timestamp, boolean success, String message, Throwable cause) {
		this.captureId = captureId;
		this.success = success;
		this.timestamp = timestamp;
		this.message = message;
		this.cause = cause;
	}

	public static CaptureStoreReceipt forSuccess(UUID captureId, Instant timestamp, String message) {
		return new CaptureStoreReceipt(captureId, timestamp, true, message, null);
	}
	
	public static CaptureStoreReceipt forFailure(UUID captureId, Instant timestamp, String message) {
		return new CaptureStoreReceipt(captureId, timestamp, false, message, null);
	}
	
	public static CaptureStoreReceipt forFailure(Throwable t) {
		return new CaptureStoreReceipt(null, Instant.now(), false, t.getMessage(), t);
	}
	
	public UUID getCaptureId() {
		return captureId;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Throwable getCause() {
		return cause;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public Instant getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + captureId + "," + isSuccess() + ", " + getTimestamp() + ", " + getMessage() + ", " + getCause() + ")";
	}

	

}
