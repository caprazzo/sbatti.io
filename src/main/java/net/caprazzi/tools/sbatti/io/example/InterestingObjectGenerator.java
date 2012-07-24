package net.caprazzi.tools.sbatti.io.example;

import net.caprazzi.tools.sbatti.io.CapturedData;
import net.caprazzi.tools.sbatti.io.IDataProbe;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Ranges;

public class InterestingObjectGenerator {

	private final IDataProbe<InterestingObject> probe;
	private final int num;

	public InterestingObjectGenerator(IDataProbe<InterestingObject> probe, int num) {
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
					probe.capture(CapturedData.forData(InterestingObject.fromInt(r)));		
				}				
			}
			
		}).start();
	}
	
	
	
}
