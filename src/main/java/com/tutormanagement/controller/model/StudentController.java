package com.tutormanagement.controller.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.tutormanagement.model.EducationLevel;
import com.tutormanagement.model.Student;
import com.tutormanagement.model.StudentReport;
import com.tutormanagement.model.dao.StudentDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.StudentSQLException;
import com.tutormanagement.model.dao.sqlite.StudentDaoSQLite;

public class StudentController {
	private static StudentController controller;

	private StudentController() {

	}

	public static StudentController getInstance() {
		if (controller == null) {
			controller = new StudentController();
		}
		return controller;
	}

	public Student createStudent(String name, String surname, String address, String phoneNumber, LocalDate birthday,
			String socialMedia, String description, EducationLevel level)
			throws ConnectionException, StudentSQLException {
		Student student = new Student(name, surname, address, phoneNumber, birthday, socialMedia, description, level);
		StudentDao studentDao = new StudentDaoSQLite();
		studentDao.createStudent(student);
		return student;
	}

	/*
	 * public void modifyStudent(String name, String surname, String address, String
	 * phoneNumber, LocalDate birthday, String socialMedia, String description,
	 * EducationLevel level) { StudentDao studentDao = new StudentDaoSQLite();
	 * studentDao.modifyStudent(student); }
	 */
	public void deleteStudent(Student student) throws ConnectionException, StudentSQLException {
		StudentDao studentDao = new StudentDaoSQLite();
		studentDao.deleteStudent(student);
	}

	public List<Student> getAllStudents() throws ConnectionException, StudentSQLException {
		StudentDao studentDao = new StudentDaoSQLite();
		return studentDao.getAllStudents();
	}

	public Map<String, List<StudentReport>> getUnpaidStudents() throws ConnectionException, StudentSQLException {
		StudentDao studentDao = new StudentDaoSQLite();
		return studentDao.getUnpaidStudents();
	}
}
