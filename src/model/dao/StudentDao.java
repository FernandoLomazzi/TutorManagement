package model.dao;

import java.util.List;
import java.util.Map;

import model.Student;
import model.StudentReport;

public interface StudentDao {
	public void createStudent(Student student);
	public void modifyStudent(Student student);
	public void deleteStudent(Student student);
	public List<Student> getAllStudents();
	public Map<String, List<StudentReport>> getUnpaidStudents();
}
