package com.tutormanagement.controller.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.tutormanagement.model.Teacher;
import com.tutormanagement.model.TeacherReport;
import com.tutormanagement.model.dao.TeacherDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.TeacherSQLException;
import com.tutormanagement.model.dao.sqlite.TeacherDaoSQLite;

public class TeacherController {
	private static TeacherController controller;

	private TeacherController() {

	}

	public static TeacherController getInstance() {
		if (controller == null) {
			controller = new TeacherController();
		}
		return controller;
	}

	public Teacher createTeacher(String name, String surname, LocalDate birthday, String description)
			throws ConnectionException, TeacherSQLException {
		Teacher teacher = new Teacher(name, surname, birthday, description);
		TeacherDao teacherDao = new TeacherDaoSQLite();
		teacherDao.createTeacher(teacher);
		return teacher;
	}

	public void deleteTeacher(Teacher teacher) throws ConnectionException, TeacherSQLException {
		TeacherDao teacherDao = new TeacherDaoSQLite();
		teacherDao.deleteTeacher(teacher);
	}

	public List<Teacher> getAllTeachers() throws ConnectionException, TeacherSQLException {
		TeacherDao teacherDao = new TeacherDaoSQLite();
		return teacherDao.getAllTeachers();
	}

	public Map<String, List<TeacherReport>> getUnpaidTeachers() throws ConnectionException, TeacherSQLException {
		TeacherDao teacherDao = new TeacherDaoSQLite();
		return teacherDao.getUnpaidTeachers();
	}
}
