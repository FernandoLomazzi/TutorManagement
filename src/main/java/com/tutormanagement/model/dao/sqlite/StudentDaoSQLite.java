package com.tutormanagement.model.dao.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.tutormanagement.model.EducationLevel;
import com.tutormanagement.model.Student;
import com.tutormanagement.model.StudentReport;
import com.tutormanagement.model.dao.StudentDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.StudentSQLException;

public class StudentDaoSQLite implements StudentDao {
	@Override
	public Map<String, List<StudentReport>> getUnpaidStudents() throws ConnectionException, StudentSQLException {
		Map<String, List<StudentReport>> studentsReport = new TreeMap<>();
		String statement = "SELECT\r\n" + "    Student.name,\r\n" + "    Student.surname,\r\n" + "    Lesson.id, \r\n"
				+ "    Lesson.total_hours,\r\n" + "    Lesson.day,\r\n" + "    Lesson.price_per_hour,\r\n"
				+ "    Teacher.name,\r\n" + "    Teacher.surname,\r\n" + "    Subject.name,\r\n"
				+ "    Institution.name,\r\n" + "    Payment.is_notified\r\n" + "FROM\r\n" + "    Payment\r\n"
				+ "INNER JOIN\r\n" + "    Student ON Student.id = Payment.id_student\r\n" + "INNER JOIN\r\n"
				+ "    Lesson ON Payment.id_lesson = Lesson.id\r\n" + "INNER JOIN\r\n"
				+ "    Subject ON Lesson.id_subject = Subject.id\r\n" + "INNER JOIN\r\n"
				+ "    Institution ON Subject.id_institution = Institution.id\r\n" + "INNER JOIN\r\n"
				+ "    Commission ON Lesson.id = Commission.id_lesson\r\n" + "INNER JOIN\r\n"
				+ "    Teacher ON Commission.id_teacher = Teacher.id\r\n" + "WHERE Payment.paid=0;";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				String studentName, studentSurname;

				Integer lessonID;
				LocalDate lessonDay;
				Double lessonPricePerHour, lessonTotalHours;

				String teacherName, teacherSurname;

				String subjectName;
				String instName;

				Boolean paymentIsNotified;

				studentName = rs.getString(1);
				studentSurname = rs.getString(2);
				lessonID = rs.getInt(3);
				lessonTotalHours = rs.getDouble(4);
				lessonDay = DateParserSQLite.parseString(rs.getString(5));
				lessonPricePerHour = rs.getDouble(6);
				teacherName = rs.getString(7);
				teacherSurname = rs.getString(8);
				subjectName = rs.getString(9);
				instName = rs.getString(10);
				paymentIsNotified = rs.getBoolean(11);
				StudentReport studentReport = new StudentReport(lessonID, studentName, studentSurname, teacherName,
						teacherSurname, subjectName, instName, lessonDay, lessonTotalHours, lessonPricePerHour,
						paymentIsNotified);
				String studentCompleteName = studentName + " " + studentSurname;
				if (studentsReport.containsKey(studentCompleteName)) {
					studentsReport.get(studentCompleteName).add(studentReport);
				} else {
					List<StudentReport> l = new ArrayList<>();
					l.add(studentReport);
					studentsReport.put(studentCompleteName, l);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StudentSQLException("Hubo un error al intentar obtener todos los alumnos con deudas", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
		return studentsReport;
	}

	@Override
	public void createStudent(Student student) throws ConnectionException, StudentSQLException {
		String statement = "INSERT INTO Student (name, surname, address, phone_number, birthday, social_media, description, education_level) VALUES (?,?,?,?,?,?,?,?)";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, student.getName());
			st.setString(2, student.getSurname());
			st.setString(3, student.getAddress());
			st.setString(4, student.getPhoneNumber());
			st.setString(5, DateParserSQLite.parseDate(student.getBirthday()));
			st.setString(6, student.getSocialMedia());
			st.setString(7, student.getDescription());
			st.setString(8, student.getEducationLevel().toString());
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StudentSQLException("Hubo un error al intentar crear al alumno", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
	}

	/*
	 * @Override public void modifyStudent(Student student) { String statement =
	 * "UPDATE Student SET address=?, phone_number=?, birthday=?, social_media=?, description=?, education_level=? WHERE name=? AND surname=?"
	 * ; Connection conn = ConnectionSQLite.connect(); try { PreparedStatement st =
	 * conn.prepareStatement(statement); st.setString(7, student.getName());
	 * st.setString(8, student.getSurname()); st.setString(1, student.getAddress());
	 * st.setString(2, student.getPhoneNumber()); st.setString(3,
	 * DateParserSQLite.parseDate(student.getBirthday())); st.setString(4,
	 * student.getSocialMedia()); st.setString(5, student.getDescription());
	 * st.setString(6, student.getEducationLevel().toString()); st.execute(); }
	 * catch (SQLException e) { e.printStackTrace(); }finally { try { conn.close();
	 * } catch (SQLException e) { e.printStackTrace(); } } }
	 */
	@Override
	public void deleteStudent(Student student) throws ConnectionException, StudentSQLException {
		String statement = "DELETE FROM Student WHERE name=? and surname=?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, student.getName());
			st.setString(2, student.getSurname());
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StudentSQLException("Hubo un error al intentar eliminar al alumno", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
	}

	@Override
	public List<Student> getAllStudents() throws ConnectionException, StudentSQLException {
		List<Student> students = new ArrayList<>();
		String statement = "SELECT name,surname,address,phone_number,birthday,social_media,description,education_level FROM Student";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				String name, surname, address, phoneNumber, socialMedia, description;
				LocalDate birthday;
				EducationLevel lvl;
				name = rs.getString(1);
				surname = rs.getString(2);
				address = rs.getString(3);
				phoneNumber = rs.getString(4);
				birthday = DateParserSQLite.parseString(rs.getString(5));
				socialMedia = rs.getString(6);
				description = rs.getString(7);
				lvl = EducationLevel.valueOf(rs.getString(8));
				Student student = new Student(name, surname, address, phoneNumber, birthday, socialMedia, description,
						lvl);
				students.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StudentSQLException("Hubo un error al intentar obtener a todos los alumnos", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}

		return students;
	}

	public static void main(String[] args) throws SQLException {
		Connection conn = ConnectionSQLite.connect();
		System.out.println(conn.isClosed());
		Student student = new Student("Pepe", "Chasau", "asd", "1234", LocalDate.now(), "!@", "223",
				EducationLevel.INGRESO);
		StudentDao studentDao = new StudentDaoSQLite();
		studentDao.createStudent(student);
		// studentDao.modifyStudent(student);
		// studentDao.deleteStudent(student);
		studentDao.getAllStudents();
		System.out.println(LocalDate.now());
		System.out.println("hola");
	}
}
