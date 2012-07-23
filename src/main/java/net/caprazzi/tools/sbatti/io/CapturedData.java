package net.caprazzi.tools.sbatti.io;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Instant;

public class CapturedData<TData> {

	private final Instant timestamp;
	private final TData data;
	private final UUID id;

	private CapturedData(UUID id, Instant timestamp, TData data) {
		this.id = id;
		this.timestamp = timestamp;
		this.data = data;		
	}
	
	public static <TData> CapturedData<TData> forData(TData data) {
		return new CapturedData<TData>(UUID.randomUUID(), new DateTime().toInstant(), data);
	}
	
	public UUID getId() {
		return id;
	}
	
	public Instant getTimestamp() {
		return timestamp;
	}
	
	public TData getData() {
		return data;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + id + ", " + timestamp + ", " + data + ")";
	}

	
	
}
