package com.tutormanagement.model.dao;

import java.util.List;
import java.util.Map;

import com.tutormanagement.model.Teacher;
import com.tutormanagement.model.TeacherReport;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.TeacherSQLException;

public interface TeacherDao {
	public void createTeacher(Teacher teacher) throws ConnectionException, TeacherSQLException;

	// public void modifyTeacher(Teacher teacher);
	public void deleteTeacher(Teacher teacher) throws ConnectionException, TeacherSQLException;

	public List<Teacher> getAllTeachers() throws ConnectionException, TeacherSQLException;

	Map<String, List<TeacherReport>> getUnpaidTeachers() throws ConnectionException, TeacherSQLException;
}
