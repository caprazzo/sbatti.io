package net.caprazzi.tools.sbatti.io.bdb;

import org.joda.time.Instant;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class CaptureDataTagEntity {

	@PrimaryKey
	private String captureId;
	private long timestamp;
	private String tagName;
	private String tagValue;
	
	public CaptureDataTagEntity() {}
	
	public CaptureDataTagEntity(String captureId, long timestamp, String tagName, String tagValue) {
		this.captureId = captureId;
		this.timestamp = timestamp;
		this.tagName = tagName;
		this.tagValue = tagValue;
	}
	
	public String getCaptureId() {
		return captureId;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public String getTagValue() {
		return tagValue;
	}

}
