package controller.model;

import java.time.LocalDate;
import java.util.List;

import model.Teacher;
import model.dao.TeacherDao;
import model.dao.sqlite.TeacherDaoSQLite;

public class TeacherController {
	private static TeacherController controller;
	
	private TeacherController() {
		
	}
	public static TeacherController getInstance() {
		if(controller==null) {
			controller = new TeacherController();
		}
		return controller;
	}
	public Teacher createTeacher(String name, String surname, LocalDate birthday, String description) {
		Teacher teacher = new Teacher(name, surname, birthday, description);
		TeacherDao teacherDao = new TeacherDaoSQLite();
		teacherDao.createTeacher(teacher);
		return teacher;
	}
	
	public void deleteTeacher(Teacher teacher) {
		TeacherDao teacherDao = new TeacherDaoSQLite();
		teacherDao.deleteTeacher(teacher);
	}
	public List<Teacher> getAllTeachers(){
		TeacherDao teacherDao = new TeacherDaoSQLite();
		return teacherDao.getAllTeachers();
	}
}
