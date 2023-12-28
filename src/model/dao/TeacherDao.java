package model.dao;

import java.util.List;

import model.Teacher;

public interface TeacherDao {
	public void createTeacher(Teacher teacher);
	public void modifyTeacher(Teacher teacher);
	public void deleteTeacher(Teacher teacher);
	public List<Teacher> getAllTeachers();
}
