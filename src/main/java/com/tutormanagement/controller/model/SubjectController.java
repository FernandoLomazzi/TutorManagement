package com.tutormanagement.controller.model;

import java.util.List;

import com.tutormanagement.model.Institution;
import com.tutormanagement.model.Subject;
import com.tutormanagement.model.dao.SubjectDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.SubjectSQLException;
import com.tutormanagement.model.dao.sqlite.SubjectDaoSQLite;

public class SubjectController {
	private static SubjectController controller;

	private SubjectController() {

	}

	public static SubjectController getInstance() {
		if (controller == null) {
			controller = new SubjectController();
		}
		return controller;
	}

	public Subject createSubject(String name, Institution inst) throws ConnectionException, SubjectSQLException {
		Subject subject = new Subject(name, inst);
		SubjectDao subjectDao = new SubjectDaoSQLite();
		subjectDao.createSubject(subject);
		return subject;
	}

	public void deleteSubject(Subject student) throws ConnectionException, SubjectSQLException {
		SubjectDao subjectDao = new SubjectDaoSQLite();
		subjectDao.deleteSubject(student);
	}

	public List<Subject> getAllSubjects() throws ConnectionException, SubjectSQLException {
		SubjectDao subjectDao = new SubjectDaoSQLite();
		return subjectDao.getAllSubjects();
	}
}
