package model;

public class Commission {
	private Lesson lesson;
	private Teacher teacher;
	private Double pricePerHour;
	private Double total;
	private Double totalPaid;
	
	public Lesson getLesson() {
		return this.lesson;
	}
}
