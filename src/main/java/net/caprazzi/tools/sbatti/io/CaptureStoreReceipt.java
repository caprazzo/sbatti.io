package net.caprazzi.tools.sbatti.io;

import org.joda.time.Instant;

public class CaptureStoreReceipt<TData> {

	private final CapturedData<TData> capture;
	private final boolean success;
	private final Instant timestamp;

	private CaptureStoreReceipt(boolean success, Instant timestamp, CapturedData<TData> capture) {
		this.success = success;
		this.timestamp = timestamp;
		this.capture = capture;
	}
	
	public static <TData> CaptureStoreReceipt<TData> forSuccess(Instant timestamp, CapturedData<TData> capture) {
		return new CaptureStoreReceipt<TData>(true, timestamp, capture);
	}
	
	public CapturedData<TData> getCapture() {
		return capture;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public Instant getTimestamp() {
		return timestamp;
	}

}
