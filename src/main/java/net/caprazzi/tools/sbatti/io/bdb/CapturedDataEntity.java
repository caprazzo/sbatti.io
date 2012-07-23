package net.caprazzi.tools.sbatti.io.bdb;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class CapturedDataEntity {	
	
	@PrimaryKey
	private String captureId;
	private Object data;
	
	public CapturedDataEntity() {}
	
	public CapturedDataEntity(String captureId, Object data) {
		this.captureId = captureId;
		this.data = data;		
	}

	public String getCaptureId() {
		return captureId;
	}
	
	public Object getData() {
		return data;
	}
	
	
}
