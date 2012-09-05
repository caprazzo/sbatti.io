package net.caprazzi.tools.sbatti.io;

import net.caprazzi.tools.sbatti.io.example.InterestingObjectDataSerizlier.DataSerializationException;

public interface IDataSerializer<TData> {
	public byte[] serialize(TData capture) throws DataSerializationException;
	public TData parse(byte[] data) throws DataSerializationException;
}
