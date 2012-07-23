package net.caprazzi.tools.sbatti.io;

public interface IDataSerializer<TData> {
	public byte[] serialize(TData capture);
	public TData parse(byte[] data);
}
