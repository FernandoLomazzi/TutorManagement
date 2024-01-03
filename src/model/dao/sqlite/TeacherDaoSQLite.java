package model.dao.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Teacher;
import model.dao.TeacherDao;

public class TeacherDaoSQLite implements TeacherDao{

	@Override
	public void createTeacher(Teacher teacher) {
		String statement = "INSERT INTO Teacher (name, surname, birthday, description) VALUES (?,?,?,?)";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, teacher.getName());
			st.setString(2, teacher.getSurname());
			st.setString(3, DateParserSQLite.parseDate(teacher.getBirthday()));
			st.setString(4, teacher.getDescription());
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
	public void modifyTeacher(Teacher teacher) {
		String statement = "UPDATE Teacher SET birthday=?, description=? WHERE name=? AND surname=?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(3, teacher.getName());
			st.setString(4, teacher.getSurname());
			st.setString(1, DateParserSQLite.parseDate(teacher.getBirthday()));
			st.setString(2, teacher.getDescription());
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
	public void deleteTeacher(Teacher teacher) {
		String statement = "DELETE FROM Teacher WHERE name=? and surname=?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, teacher.getName());
			st.setString(2, teacher.getSurname());
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
	public List<Teacher> getAllTeachers() {
		List<Teacher> teachers = new ArrayList<>();
		String statement = "SELECT name,surname,birthday,description FROM Teacher";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				String name, surname, description;
				LocalDate birthday;
				name = rs.getString(1);
				surname = rs.getString(2);
				birthday = DateParserSQLite.parseString(rs.getString(3));
				description = rs.getString(4);
				Teacher teacher = new Teacher(name,surname,birthday,description);
				teachers.add(teacher);
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
		System.out.println(teachers);
		
		return teachers;
	}
	public static void main(String[] args) throws SQLException {
		Connection conn = ConnectionSQLite.connect();
		System.out.println(conn.isClosed());
		Teacher teacher = new Teacher("Holas", "Chasau", LocalDate.now(), "223");
		Teacher teacher2 = new Teacher("Holas", "Chasau", LocalDate.now(), "123");
		TeacherDao teacherDao = new TeacherDaoSQLite();
		//teacherDao.createTeacher(teacher);
		//teacherDao.modifyTeacher(teacher2);
		//teacherDao.deleteTeacher(teacher);
		teacherDao.getAllTeachers();
	}
}
