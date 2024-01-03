package model.dao;

import java.util.List;

import model.Subject;

public interface SubjectDao {
	public void createSubject(Subject subject);
	public void deleteSubject(Subject subject);
	public List<Subject> getAllSubjects();
}
