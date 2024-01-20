package com.tutormanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class TeacherReport {
	String teacherName, teacherSurname;
	Double total, totalHours;
	Integer lessonID;
	LocalDate day;
	String subjectName, instName;
	List<String> students;

	public TeacherReport(String teacherName, String teacherSurname, Double total, Integer id, LocalDate day,
			Double totalHours, String subjectName, String instName) {
		this.teacherName = teacherName;
		this.teacherSurname = teacherSurname;
		this.total = total;
		this.lessonID = id;
		this.day = day;
		this.totalHours = totalHours;
		this.subjectName = subjectName;
		this.instName = instName;
		this.students = new ArrayList<>();
	}

	@Override
	public String toString() {
		return teacherName + " " + teacherSurname + " " + students.toString();
	}

	public String getTeacherName() {
		return teacherName;
	}

	public String getTeacherSurname() {
		return teacherSurname;
	}

	public Double getTotal() {
		return total;
	}

	public Double getTotalHours() {
		return totalHours;
	}

	public Integer getLessonID() {
		return lessonID;
	}

	public LocalDate getDay() {
		return day;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public String getInstName() {
		return instName;
	}

	public List<String> getStudents() {
		return students;
	}

	public String getStudentsName() {
		StringJoiner joiner = new StringJoiner(", ");
		students.forEach(s -> joiner.add(s));
		return joiner.toString();
	}

	public void addStudent(String name) {
		students.add(name);
	}

}
