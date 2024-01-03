package model;

import java.time.LocalDate;
import java.util.List;

import io.github.palexdev.materialfx.utils.FXCollectors;

public class Lesson {
	public static Integer actualId;
	private Integer id;
	private Integer totalHours;
	private LocalDate day;
	private Double pricePerHour;
	private LessonState state;
	private List<Payment> payments;
	private Commission commission;
	private Subject subject;
	
	public Lesson() {
		state = LessonState.UNPAID;
		id = actualId++;
	}
	public Lesson(Integer id, Integer totalHours, LocalDate day, Double pricePerHour, LessonState state, List<Payment> payments, Commission commission, Subject subject) {
		
	}
	public void updateState() {
		Boolean studentPaid = true, teacherPaid = commission.isPaid();
		for(Payment p: payments)
			studentPaid &= p.isPaid();
		if(studentPaid && teacherPaid) {
			state = LessonState.PAID;
		} else if(studentPaid && !teacherPaid) {
			state = LessonState.TEACHERUNPAID;
		} else if(!studentPaid && teacherPaid) {
			state = LessonState.STUDENTUNPAID;
		} else {
			state = LessonState.UNPAID;
		}
	}
	public Teacher getTeacher() {
		return commission.getTeacher();
	}
	public List<Student> getStudents(){
		return payments.stream().map(p -> p.getStudent()).toList();
	}
	public Integer getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(Integer totalHours) {
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
	public LessonState getState() {
		return state;
	}
	public void setState(LessonState state) {
		this.state = state;
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
		return pricePerHour*totalHours;
	}
	public Double amountToPayTeacher() {
		return this.getCommission().getTotal();
	}
}
