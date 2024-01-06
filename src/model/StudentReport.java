package model;

import java.time.LocalDate;

public class StudentReport {
	private String studentName, studentSurname;
	private String teacherName, teacherSurname, subjectName, institutionName;
	private LocalDate day;
	private Integer lessonID, totalHours;
	private Double pricePerHour;
	private Boolean notified;
	
	public StudentReport(Integer lessonID, String studentName, String studentSurname, String teacherName, String teacherSurname,
			String subjectName,	String institutionName, LocalDate day, Integer totalHours, Double pricePerHour, Boolean notified) {
		this.lessonID = lessonID;
		this.studentName = studentName;
		this.studentSurname = studentSurname;
		this.teacherName = teacherName;
		this.teacherSurname = teacherSurname;
		this.subjectName = subjectName;
		this.institutionName = institutionName;
		this.day = day;
		this.totalHours = totalHours;
		this.pricePerHour = pricePerHour;
		this.notified = notified;
	}
	public void setNotified(Boolean notified) {
		this.notified = notified;
	}
	public Boolean isNotified() {
		return notified;
	}
	public String getStudentCompleteName() {
		return studentName + " " + studentSurname;
	}

	public String getTeacherCompleteName() {
		return teacherName + " " + teacherSurname;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public LocalDate getDay() {
		return day;
	}

	public Integer getTotalHours() {
		return totalHours;
	}

	public Double getPricePerHour() {
		return pricePerHour;
	}
	public Double getTotal() {
		return totalHours*pricePerHour;
	}
	public Integer getLessonID() {
		return lessonID;
	}
	public String getStudentName() {
		return studentName;
	}
	public String getStudentSurname() {
		return studentSurname;
	}
}
