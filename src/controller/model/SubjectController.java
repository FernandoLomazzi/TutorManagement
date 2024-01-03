package controller.model;

import java.util.List;

import model.Institution;
import model.Subject;
import model.dao.SubjectDao;
import model.dao.sqlite.SubjectDaoSQLite;

public class SubjectController {
	private static SubjectController controller;
	
	private SubjectController() {
		
	}
	public static SubjectController getInstance() {
		if(controller==null) {
			controller = new SubjectController();
		}
		return controller;
	}
	public Subject createSubject(String name, Institution inst) {
		Subject subject = new Subject(name, inst);
		SubjectDao subjectDao = new SubjectDaoSQLite();
		subjectDao.createSubject(subject);
		return subject;
	}
	public void deleteSubject(Subject student) {
		SubjectDao subjectDao = new SubjectDaoSQLite();
		subjectDao.deleteSubject(student);
	}
	public List<Subject> getAllSubjects() {
		SubjectDao subjectDao = new SubjectDaoSQLite();
		return subjectDao.getAllSubjects();
	}
}
