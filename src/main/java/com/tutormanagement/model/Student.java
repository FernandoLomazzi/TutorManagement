package com.tutormanagement.model;

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
	private EducationLevel lvl;
	private List<Payment> payments;

	public Student() {
		;
	}

	public Student(String name, String surname, String address, String phoneNumber, LocalDate birthday,
			String socialMedia, String description, EducationLevel lvl) {
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.birthday = birthday;
		this.socialMedia = socialMedia;
		this.description = description;
		this.lvl = lvl;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public String getSocialMedia() {
		return socialMedia;
	}

	public String getDescription() {
		return description;
	}

	public EducationLevel getEducationLevel() {
		return lvl;
	}

	public String getEducationLevelInitial() {
		return this.getEducationLevel().toString();
		// return lvl.toString().substring(0, 1);
	}

	/*
	 * public List<Lesson> getLessons(){ return this.payments.stream().map(p ->
	 * p.getLesson()).collect(Collectors.toList()); }
	 */
}