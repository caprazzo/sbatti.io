package net.caprazzi.tools.sbatti.io.example;

import net.caprazzi.tools.sbatti.io.IDataSerializer;


public class InterestingObjectDataSerizlier implements
		IDataSerializer<InterestingObject> {

	@Override
	public byte[] serialize(InterestingObject capture) throws DataSerializationException {
		try {
			return new Integer(capture.getId()).toString().getBytes("UTF-8");
		} catch (Exception e) {
			throw new DataSerializationException(e);
		}
	}

	@Override
	public InterestingObject parse(byte[] data) throws DataSerializationException {
		try {
			return InterestingObject.fromInt(Integer.parseInt(new String(data, "UTF-8")));
		} catch (Exception e) {
			throw new DataSerializationException(e);
		}
	}
	
	@SuppressWarnings("serial")
	public static class DataSerializationException extends Exception {
		public DataSerializationException(Exception e) {
			super(e);
		}
	}

}
