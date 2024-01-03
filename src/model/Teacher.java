package model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Teacher {
	private String name;
	private String surname;
	private LocalDate birthday;
	private String description;
	private List<Commission> commissions;
	
	public Teacher(String name, String surname, LocalDate birthday, String description) {
		this.name = name;
		this.surname = surname;
		this.birthday = birthday;
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public LocalDate getBirthday() {
		return birthday;
	}
	public String getDescription() {
		return description;
	}
	@Override
	public String toString() {
		return name+" "+surname;
	}
	/*
	public List<Lesson> getLessons(){
		return this.commissions.stream().map(c -> c.getLesson()).collect(Collectors.toList());
	}*/
}
