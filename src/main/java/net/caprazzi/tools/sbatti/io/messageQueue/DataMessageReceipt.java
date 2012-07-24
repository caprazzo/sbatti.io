package net.caprazzi.tools.sbatti.io.messageQueue;

import java.util.UUID;

import org.joda.time.Instant;

public class DataMessageReceipt {

	private final boolean success;
	private final Instant timestamp;
	private final UUID captureId;
	private final String message;
	private final Throwable cause;

	private DataMessageReceipt(UUID captureId, Instant timestamp, boolean success, String message, Throwable cause) {
		this.captureId = captureId;
		this.success = success;
		this.timestamp = timestamp;
		this.message = message;
		this.cause = cause;
	}

	public static DataMessageReceipt forSuccess(UUID captureId, Instant timestamp, String message) {
		return new DataMessageReceipt(captureId, timestamp, true, message, null);
	}
	
	public static DataMessageReceipt forFailure(UUID captureId, Instant timestamp, String message) {
		return new DataMessageReceipt(captureId, timestamp, false, message, null);
	}
	
	public static DataMessageReceipt forFailure(Throwable t) {
		return new DataMessageReceipt(null, Instant.now(), false, t.getMessage(), t);
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
