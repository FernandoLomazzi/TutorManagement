package com.tutormanagement.model.dao;

import java.time.LocalDate;
import java.util.List;

import com.tutormanagement.model.Lesson;
import com.tutormanagement.model.StudentReport;
import com.tutormanagement.model.TeacherReport;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.LessonSQLException;
import com.tutormanagement.model.dao.exception.StudentSQLException;
import com.tutormanagement.model.dao.exception.TeacherSQLException;

public interface LessonDao {
	public void createLesson(Lesson lesson) throws ConnectionException, LessonSQLException;

	public void deleteLesson(Integer idLesson) throws ConnectionException, LessonSQLException;

	public Integer getLessonID() throws ConnectionException, LessonSQLException;

	public void setNotified(StudentReport student, Boolean notified) throws ConnectionException, StudentSQLException;

	void makePayment(StudentReport student) throws ConnectionException, StudentSQLException;

	void makePayment(TeacherReport teacherReport) throws ConnectionException, TeacherSQLException;

	List<TeacherReport> getLessons() throws ConnectionException, LessonSQLException;

	void modifyLesson(Integer idLesson, LocalDate day, Double totalHours, Double pricePerHour,
			Double pricePerHourTeacher, Double double1) throws ConnectionException, LessonSQLException;
}
