package model.dao;

import java.util.List;

import model.Student;

public interface StudentDao {
	public void createStudent(Student student);
	public void modifyStudent(Student student);
	public void deleteStudent(Student student);
	public List<Student> getAllStudents();
}
