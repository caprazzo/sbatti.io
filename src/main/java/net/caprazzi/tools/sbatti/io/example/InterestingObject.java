package net.caprazzi.tools.sbatti.io.example;


public class InterestingObject {

	private final int id;
	
	public static InterestingObject fromInt(int id) {
		return new InterestingObject(id);
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
