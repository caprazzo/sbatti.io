package net.caprazzi.tools.sbatti.io.core;

import net.caprazzi.tools.sbatti.io.CapturedData;
import net.caprazzi.tools.sbatti.io.IDataProbe;
import net.caprazzi.tools.sbatti.io.IDataSerializer;
import net.caprazzi.tools.sbatti.io.bdb.CapturedDataEntity;
import net.caprazzi.tools.sbatti.io.example.InterestingObjectDataSerizlier.DataSerializationException;

public class BdbRetryDataProbe<TData> {

	private IDataSerializer<TData> serializer;
	private IDataProbe<TData> probe;

	public BdbRetryDataProbe(IDataSerializer<TData> serializer, IDataProbe<TData> probe) {
		this.serializer = serializer;
		this.probe = probe;
	}
	
	public void capture(CapturedDataEntity data) {		
		probe.capture(asCapturedData(data));		
	}

	private CapturedData<TData> asCapturedData(CapturedDataEntity entity) {
		return CapturedDataEntity.toCapture(serializer, entity);
	}

}
