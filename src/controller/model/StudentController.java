package controller.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import model.EducationLevel;
import model.Student;
import model.StudentReport;
import model.dao.StudentDao;
import model.dao.sqlite.StudentDaoSQLite;

public class StudentController {
	private static StudentController controller;
	
	private StudentController() {
		
	}
	public static StudentController getInstance() {
		if(controller==null) {
			controller = new StudentController();
		}
		return controller;
	}
	public Student createStudent(String name, String surname, String address, String phoneNumber, LocalDate birthday, String socialMedia, String description, EducationLevel level) {
		Student student = new Student(name, surname, address, phoneNumber, birthday, socialMedia, description, level);
		StudentDao studentDao = new StudentDaoSQLite();
		studentDao.createStudent(student);
		return student;
	}
	/*public void modifyStudent(String name, String surname, String address, String phoneNumber, LocalDate birthday, String socialMedia, String description, EducationLevel level) {
		StudentDao studentDao = new StudentDaoSQLite();
		studentDao.modifyStudent(student);		
	}*/
	public void deleteStudent(Student student) {
		StudentDao studentDao = new StudentDaoSQLite();
		studentDao.deleteStudent(student);
	}
	public List<Student> getAllStudents() {
		StudentDao studentDao = new StudentDaoSQLite();
		return studentDao.getAllStudents();
	}
	public Map<String, List<StudentReport>> getUnpaidStudents(){
		StudentDao studentDao = new StudentDaoSQLite();
		return studentDao.getUnpaidStudents();
	}
}
