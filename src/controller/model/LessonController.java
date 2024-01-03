package controller.model;

import model.Lesson;
import model.StudentReport;
import model.dao.LessonDao;
import model.dao.sqlite.LessonDaoSQLite;

public class LessonController {
	private static LessonController controller;
	
	private LessonController() {
		LessonDao lessonDao = new LessonDaoSQLite();
		Lesson.actualId = lessonDao.getLessonID();
	}
	public static LessonController getInstance() {
		if(controller==null) {
			controller = new LessonController();
		}
		return controller;
	}
	public void createLesson(Lesson lesson) {
		LessonDao lessonDao = new LessonDaoSQLite();
		lessonDao.createLesson(lesson);
	}

	public void makePayment(StudentReport student) {
		LessonDao lessonDao = new LessonDaoSQLite();
		lessonDao.makePayment(student);
	}
	public void setNotified(StudentReport student, Boolean notified) {
		LessonDao lessonDao = new LessonDaoSQLite();
		lessonDao.setNotified(student, notified);
	}
}
