package com.tutormanagement.model;

public class Commission {
	private Lesson lesson;
	private Teacher teacher;
	private Double pricePerHour;
	private Double total;
	private Boolean paid;

	public Commission(Lesson lesson, Teacher teacher, Double pricePerHour, Double total, Boolean paid) {
		this.lesson = lesson;
		this.teacher = teacher;
		this.pricePerHour = pricePerHour;
		this.total = total;
		this.paid = paid;
	}

	public Boolean isPaid() {
		return paid;
	}

	public Lesson getLesson() {
		return this.lesson;
	}

	public Double getPricePerHour() {
		return pricePerHour;
	}

	public Double getTotal() {
		return total;
	}

	public Teacher getTeacher() {
		return teacher;
	}
}
