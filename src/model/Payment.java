package model;

public class Payment {
	private Student student;
	private Lesson lesson;
	private Boolean paid;
	private Boolean notified;
	
	public Payment(Student student, Lesson lesson, Boolean paid, Boolean notified) {
		this.student = student;
		this.lesson = lesson;
		this.paid = paid;
		this.notified = notified;
	}
	public Boolean isNotified() {
		return notified;
	}
	public Boolean isPaid() {
		return paid;
	}
	public Lesson getLesson() {
		return this.lesson;
	}
	public Student getStudent() {
		return student;
	}
	
}
