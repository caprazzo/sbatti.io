package learn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.caprazzi.tools.sbatti.io.Capture;
import net.caprazzi.tools.sbatti.io.Capture.Captured;

public class LearnProtocolBuffers {

	public static void main(String[] args) throws IOException {
		Captured build = Capture.Captured
			.newBuilder()
			.setId("my id")
			.build();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		build.writeTo(baos);
		
		System.out.println("::" + new String(baos.toByteArray()) + "::");
		
		
		Captured parseFrom = Capture.Captured.parseFrom(baos.toByteArray());
		System.out.println(parseFrom.getId());
		
		
	}
	
}
