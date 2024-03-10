package com.tutormanagement.controller.model;

import java.util.List;

import com.tutormanagement.model.Lesson;
import com.tutormanagement.model.StudentReport;
import com.tutormanagement.model.TeacherReport;
import com.tutormanagement.model.dao.LessonDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.LessonSQLException;
import com.tutormanagement.model.dao.exception.StudentSQLException;
import com.tutormanagement.model.dao.exception.TeacherSQLException;
import com.tutormanagement.model.dao.sqlite.LessonDaoSQLite;

public class LessonController {
	private static LessonController controller;

	private LessonController() throws ConnectionException, LessonSQLException {
		LessonDao lessonDao = new LessonDaoSQLite();
		Lesson.actualId = lessonDao.getLessonID();
	}
	
	public static LessonController getInstance() throws ConnectionException, LessonSQLException {
		if (controller == null) {
			controller = new LessonController();
		}
		return controller;
	}
	public void modifyLesson(TeacherReport lesson) throws ConnectionException, LessonSQLException {
		LessonDao lessonDao = new LessonDaoSQLite();
		lessonDao.modifyLesson(lesson.getLessonID(), lesson.getDay(), lesson.getTotalHours(),
				lesson.getStudentHourPrice(), lesson.getTeacherHourPrice(), lesson.getTotal());
	}
	public void deleteLesson(Integer idLesson) throws ConnectionException, LessonSQLException {
		LessonDao lessonDao = new LessonDaoSQLite();
		lessonDao.deleteLesson(idLesson);
	}
	public List<TeacherReport> getLessons() throws ConnectionException, LessonSQLException{
		LessonDao lessonDao = new LessonDaoSQLite();
		return lessonDao.getLessons();
	}
	public void createLesson(Lesson lesson) throws ConnectionException, LessonSQLException {
		LessonDao lessonDao = new LessonDaoSQLite();
		lessonDao.createLesson(lesson);
	}

	public void makePayment(StudentReport student) throws ConnectionException, StudentSQLException {
		LessonDao lessonDao = new LessonDaoSQLite();
		lessonDao.makePayment(student);
	}

	public void makePayment(TeacherReport teacher) throws ConnectionException, TeacherSQLException {
		LessonDao lessonDao = new LessonDaoSQLite();
		lessonDao.makePayment(teacher);
	}

	public void setNotified(StudentReport student, Boolean notified) throws ConnectionException, StudentSQLException {
		LessonDao lessonDao = new LessonDaoSQLite();
		lessonDao.setNotified(student, notified);
	}
}
