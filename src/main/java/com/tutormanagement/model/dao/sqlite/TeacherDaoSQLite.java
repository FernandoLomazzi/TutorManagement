package com.tutormanagement.model.dao.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tutormanagement.model.Teacher;
import com.tutormanagement.model.TeacherReport;
import com.tutormanagement.model.dao.TeacherDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.TeacherSQLException;

public class TeacherDaoSQLite implements TeacherDao {
	@Override
	public Map<String, List<TeacherReport>> getUnpaidTeachers() throws ConnectionException, TeacherSQLException {
		Map<String, List<TeacherReport>> teachersReport = new HashMap<>();
		String statement = "SELECT\r\n" + "    Teacher.name,\r\n" + "    Teacher.surname,\r\n"
				+ "    Commission.total,\r\n" + "    Lesson.id,\r\n" + "    Lesson.day,\r\n"
				+ "    Lesson.total_hours,\r\n" + "    Subject.name,\r\n" + "    Institution.name\r\n" + "FROM\r\n"
				+ "    Commission\r\n" + "INNER JOIN\r\n" + "    Teacher ON Commission.id_teacher = Teacher.id\r\n"
				+ "INNER JOIN\r\n" + "    Lesson ON Lesson.id = Commission.id_lesson\r\n" + "INNER JOIN\r\n"
				+ "    Subject ON Lesson.id_subject = Subject.id\r\n" + "INNER JOIN\r\n"
				+ "    Institution ON Subject.id_institution = Institution.id\r\n" + "WHERE Commission.paid=0;";
		Connection conn = ConnectionSQLite.connect();
		try {
			conn.setAutoCommit(false);
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				String teacherName, teacherSurname;

				Double lessonTotalHours;
				Integer lessonID;
				LocalDate lessonDay;

				String subjectName;
				String instName;

				Double commissionTotal;

				teacherName = rs.getString(1);
				teacherSurname = rs.getString(2);
				commissionTotal = rs.getDouble(3);
				lessonID = rs.getInt(4);
				lessonDay = DateParserSQLite.parseString(rs.getString(5));
				lessonTotalHours = rs.getDouble(6);
				subjectName = rs.getString(7);
				instName = rs.getString(8);
				TeacherReport teacherReport = new TeacherReport(teacherName, teacherSurname, commissionTotal, lessonID,
						lessonDay, lessonTotalHours, subjectName, instName);
				String teacherCompleteName = teacherName + " " + teacherSurname;
				if (teachersReport.containsKey(teacherCompleteName)) {
					teachersReport.get(teacherCompleteName).add(teacherReport);
				} else {
					List<TeacherReport> l = new ArrayList<>();
					l.add(teacherReport);
					teachersReport.put(teacherCompleteName, l);
				}
			}
			String statementStudent = "SELECT Student.name, Student.surname FROM Student "
					+ "INNER JOIN Payment ON Student.id=Payment.id_student WHERE Payment.id_lesson=?";
			for (List<TeacherReport> reports : teachersReport.values()) {
				for (TeacherReport report : reports) {
					PreparedStatement stat = conn.prepareStatement(statementStudent);
					stat.setInt(1, report.getLessonID());
					ResultSet rset = stat.executeQuery();
					while (rset.next()) {
						String studentName = rset.getString(1);
						String studentSurname = rset.getString(2);
						report.addStudent(studentName + " " + studentSurname);
					}
				}
			}
			conn.commit();
		} catch (SQLException e) {
			throw new TeacherSQLException("Hubo un error al intentar obtener todos los profesores deudores", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}

		return teachersReport;
	}

	@Override
	public void createTeacher(Teacher teacher) throws ConnectionException, TeacherSQLException {
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
			throw new TeacherSQLException("Hubo un error al intentar crear al profesor", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
	}

	/*
	 * @Override public void modifyTeacher(Teacher teacher) { String statement =
	 * "UPDATE Teacher SET birthday=?, description=? WHERE name=? AND surname=?";
	 * Connection conn = ConnectionSQLite.connect(); try { PreparedStatement st =
	 * conn.prepareStatement(statement); st.setString(3, teacher.getName());
	 * st.setString(4, teacher.getSurname()); st.setString(1,
	 * DateParserSQLite.parseDate(teacher.getBirthday())); st.setString(2,
	 * teacher.getDescription()); st.execute(); } catch (SQLException e) {
	 * e.printStackTrace(); }finally { try { conn.close(); } catch (SQLException e)
	 * { e.printStackTrace(); } } }
	 */
	@Override
	public void deleteTeacher(Teacher teacher) throws ConnectionException, TeacherSQLException {
		String statement = "DELETE FROM Teacher WHERE name=? and surname=?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, teacher.getName());
			st.setString(2, teacher.getSurname());
			st.execute();
		} catch (SQLException e) {
			throw new TeacherSQLException("Hubo un error al intentar eliminar al profesor", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
	}

	@Override
	public List<Teacher> getAllTeachers() throws ConnectionException, TeacherSQLException {
		List<Teacher> teachers = new ArrayList<>();
		String statement = "SELECT name,surname,birthday,description FROM Teacher";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				String name, surname, description;
				LocalDate birthday;
				name = rs.getString(1);
				surname = rs.getString(2);
				birthday = DateParserSQLite.parseString(rs.getString(3));
				description = rs.getString(4);
				Teacher teacher = new Teacher(name, surname, birthday, description);
				teachers.add(teacher);
			}
		} catch (SQLException e) {
			throw new TeacherSQLException("Hubo un error al intentar obtener a todos los profesores", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}

		return teachers;
	}

	public static void main(String[] args) throws SQLException {
		Connection conn = ConnectionSQLite.connect();
		System.out.println(conn.isClosed());
		Teacher teacher = new Teacher("Holas", "Chasau", LocalDate.now(), "223");
		Teacher teacher2 = new Teacher("Holas", "Chasau", LocalDate.now(), "123");
		TeacherDao teacherDao = new TeacherDaoSQLite();
		// teacherDao.createTeacher(teacher);
		// teacherDao.modifyTeacher(teacher2);
		// teacherDao.deleteTeacher(teacher);
		teacherDao.getAllTeachers();
	}
}
