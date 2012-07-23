package net.caprazzi.tools.sbatti.io.example;

import java.nio.charset.Charset;

import com.google.common.base.Charsets;

import net.caprazzi.tools.sbatti.io.IDataSerializer;


public class InterestingObjectDataSerizlier implements
		IDataSerializer<InterestingObject> {

	@Override
	public byte[] serialize(InterestingObject capture) {
		return capture.toString().getBytes(Charsets.UTF_8);
	}

	@Override
	public InterestingObject parse(byte[] data) {
		return InterestingObject.newObject();
	}

}
