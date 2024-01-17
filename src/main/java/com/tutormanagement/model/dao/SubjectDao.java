package com.tutormanagement.model.dao;

import java.util.List;

import com.tutormanagement.model.Subject;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.SubjectSQLException;

public interface SubjectDao {
	public void createSubject(Subject subject) throws ConnectionException, SubjectSQLException;

	public void deleteSubject(Subject subject) throws ConnectionException, SubjectSQLException;

	public List<Subject> getAllSubjects() throws ConnectionException, SubjectSQLException;
}
