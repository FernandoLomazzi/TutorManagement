package model.dao.sqlite;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import model.EducationLevel;
import model.Student;
import model.dao.StudentDao;

public class StudentDaoSQLite implements StudentDao {
	@Override
	public void createStudent(Student student) {
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
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void modifyStudent(Student student) {
		String statement = "UPDATE Student SET address=?, phone_number=?, birthday=?, social_media=?, description=?, education_level=? WHERE name=? AND surname=?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(7, student.getName());
			st.setString(8, student.getSurname());
			st.setString(1, student.getAddress());
			st.setString(2, student.getPhoneNumber());
			st.setString(3, DateParserSQLite.parseDate(student.getBirthday()));
			st.setString(4, student.getSocialMedia());
			st.setString(5, student.getDescription());
			st.setString(6, student.getEducationLevel().toString());
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void deleteStudent(Student student) {
		String statement = "DELETE FROM Student WHERE name=? and surname=?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, student.getName());
			st.setString(2, student.getSurname());
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public List<Student> getAllStudents() {
		List<Student> students = new ArrayList<>();
		String statement = "SELECT name,surname,address,phone_number,birthday,social_media,description,education_level FROM Student";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
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
				Student student = new Student(name,surname,address,phoneNumber,birthday,socialMedia,description,lvl);
				students.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println(students);
		
		return students;
	}
	public static void main(String[] args) throws SQLException {
		Connection conn = ConnectionSQLite.connect();
		System.out.println(conn.isClosed());
		Student student = new Student("Pepe", "Chasau", "asd", "1234", LocalDate.now(), "!@", "223", EducationLevel.INGRESO);
		StudentDao studentDao = new StudentDaoSQLite();
		studentDao.createStudent(student);
		//studentDao.modifyStudent(student);
		//studentDao.deleteStudent(student);
		studentDao.getAllStudents();
		System.out.println(LocalDate.now());
		System.out.println("hola");
	}
}

