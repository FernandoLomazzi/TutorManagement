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
	/*
	public List<Lesson> getLessons(){
		return this.commissions.stream().map(c -> c.getLesson()).collect(Collectors.toList());
	}*/
}
