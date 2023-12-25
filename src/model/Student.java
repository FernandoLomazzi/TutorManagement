package model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Student {
	private String name;
	private String surname;
	private String address;
	private String phoneNumber;
	private LocalDate birthday;
	private String socialMedia;
	private String description;
	private List<Payment> payments;
	/*
	public List<Lesson> getLessons(){
		return this.payments.stream().map(p -> p.getLesson()).collect(Collectors.toList());
	}*/
}