package model;

public class Institution {
	private String name;
	public Institution(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() { 
		return name;
	}
}
