package net.caprazzi.tools.sbatti.io.bdb;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Instant;

import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.example.InterestingObjectDataSerizlier.DataSerializationException;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessage;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class CapturedDataEntity {	
	
	@PrimaryKey
	private String captureId;
	
	@SecondaryKey(relate = Relationship.MANY_TO_ONE)
	private CaptureStatus status;  
	
	private Object data;
	
	private long timestamp;
	
	public CapturedDataEntity() {}
	
	public CapturedDataEntity(String captureId, CaptureStatus status, long timestamp, Object data) {
		this.captureId = captureId;
		this.status = status;
		this.timestamp = timestamp;
		this.data = data;		
	}
	
	public void setStatus(CaptureStatus status) {
		this.status = status;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setData(Object data) {
		this.data = data;
	}

	public String getCaptureId() {
		return captureId;
	}
	
	public Object getData() {
		return data;
	}
	
	public CaptureStatus getStatus() {
		return status;
	}
	
	public static <TData> DataMessage<TData> toCapture(IDataSerializer<TData> serializer, CapturedDataEntity entity) {
		try {
			return DataMessage.forData(UUID.fromString(entity.captureId), Instant.now(), serializer.parse((byte[])entity.getData()));
		} catch (DataSerializationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Timestamp of the last status update
	 * @return
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
}
