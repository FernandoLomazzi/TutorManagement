package model.dao;

import model.Lesson;
import model.StudentReport;

public interface LessonDao {
	public void createLesson(Lesson lesson);
	public void deleteLesson(Lesson lesson);
	public Integer getLessonID();
	void makePayment(StudentReport student);
	public void setNotified(StudentReport student, Boolean notified);
}
