package net.caprazzi.tools.sbatti.io.example;

import net.caprazzi.tools.sbatti.io.MessageCollector;
import net.caprazzi.tools.sbatti.io.messageQueue.DataMessage;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Ranges;

public class InterestingObjectGenerator {

	private final MessageCollector<InterestingObject> probe;
	private final int num;

	public InterestingObjectGenerator(MessageCollector<InterestingObject> probe, int num) {
		this.probe = probe;
		this.num = num;
	}
	
	public void start() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ContiguousSet<Integer> set = Ranges
						.openClosed(0, num)
						.asSet(DiscreteDomains.integers());
				
				for (int r : set) {			
					probe.collect(DataMessage.forData(InterestingObject.fromInt(r)));		
				}				
			}
			
		}).start();
	}
	
	
	
}
