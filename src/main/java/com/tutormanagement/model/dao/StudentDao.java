package com.tutormanagement.model.dao;

import java.util.List;
import java.util.Map;

import com.tutormanagement.model.Student;
import com.tutormanagement.model.StudentReport;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.StudentSQLException;

public interface StudentDao {
	public void createStudent(Student student) throws ConnectionException, StudentSQLException;

	// public void modifyStudent(Student student);
	public void deleteStudent(Student student) throws ConnectionException, StudentSQLException;

	public List<Student> getAllStudents() throws ConnectionException, StudentSQLException;

	public Map<String, List<StudentReport>> getUnpaidStudents() throws ConnectionException, StudentSQLException;
}
