package com.tutormanagement.model;

import java.time.LocalDate;
import java.util.List;

public class Lesson {
	public static Integer actualId;
	private Integer id;
	private Double totalHours;
	private LocalDate day;
	private Double pricePerHour;
	private List<Payment> payments;
	private Commission commission;
	private Subject subject;

	public Lesson() {
		id = actualId++;
	}

	public Teacher getTeacher() {
		return commission.getTeacher();
	}

	public List<Student> getStudents() {
		return payments.stream().map(p -> p.getStudent()).toList();
	}

	public Double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public Double getPricePerHour() {
		return pricePerHour;
	}

	public void setPricePerHour(Double pricePerHour) {
		this.pricePerHour = pricePerHour;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayment(List<Payment> payments) {
		this.payments = payments;
	}

	public Commission getCommission() {
		return commission;
	}

	public void setCommission(Commission commission) {
		this.commission = commission;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Integer getId() {
		return id;
	}

	public Double amountToPayStudent() {
		return pricePerHour * totalHours;
	}

	public Double amountToPayTeacher() {
		return this.getCommission().getTotal();
	}
}
