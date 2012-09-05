package net.caprazzi.tools.sbatti.io.core;

import net.caprazzi.tools.sbatti.io.MessageCollector;
import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.bdb.CapturedDataEntity;
import net.caprazzi.tools.sbatti.io.example.InterestingObjectDataSerizlier.DataSerializationException;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessage;

public class BdbRetryDataProbe<TData> {

	private IDataSerializer<TData> serializer;
	private MessageCollector<TData> probe;

	public BdbRetryDataProbe(IDataSerializer<TData> serializer, MessageCollector<TData> probe) {
		this.serializer = serializer;
		this.probe = probe;
	}
	
	public void capture(CapturedDataEntity data) {		
		probe.collect(asCapturedData(data));		
	}

	private DataMessage<TData> asCapturedData(CapturedDataEntity entity) {
		return CapturedDataEntity.toCapture(serializer, entity);
	}

}
