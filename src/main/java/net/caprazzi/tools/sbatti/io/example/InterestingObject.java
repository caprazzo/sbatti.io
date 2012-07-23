package net.caprazzi.tools.sbatti.io.example;

import java.util.concurrent.atomic.AtomicInteger;

public class InterestingObject {

	private final int id;
	
	private static AtomicInteger counter = new AtomicInteger();
	
	public static InterestingObject newObject() {
		return new InterestingObject(counter.incrementAndGet());
	}
	
	private InterestingObject(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + id + "]-" + hashCode(); 
	}
	
}
