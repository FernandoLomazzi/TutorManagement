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
import java.util.TreeMap;

import com.tutormanagement.model.EducationLevel;
import com.tutormanagement.model.Teacher;
import com.tutormanagement.model.TeacherReport;
import com.tutormanagement.model.dao.TeacherDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.TeacherSQLException;

public class TeacherDaoSQLite implements TeacherDao {
	@Override
	public Map<String, List<TeacherReport>> getUnpaidTeachers() throws ConnectionException, TeacherSQLException {
		Map<String, List<TeacherReport>> teachersReport = new TreeMap<>();
		String statement = "SELECT\r\n" + "    Teacher.name,\r\n" + "    Teacher.surname,\r\n"
				+ "    Commission.total,\r\n" + "    Lesson.id,\r\n" + "    Lesson.day,\r\n"
				+ "    Lesson.total_hours, Lesson.price_per_hour, \r\n" + "    Subject.name,\r\n" + "    Institution.name\r\n" + "FROM\r\n"
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

				Double lessonTotalHours, pricePerHourStudent;
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
				pricePerHourStudent = rs.getDouble(7);
				subjectName = rs.getString(8);
				instName = rs.getString(9);
				TeacherReport teacherReport = new TeacherReport(teacherName, teacherSurname, commissionTotal, lessonID,
						lessonDay, lessonTotalHours, pricePerHourStudent, subjectName, instName);
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
			throw new TeacherSQLException("Hubo un error al intentar obtener a todos los profesores", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}

		return teachers;
	}
	
	@Override
	public Map<EducationLevel, Double> getTotalIncomePerLevel(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException {
		Map<EducationLevel, Double> inc = new HashMap<>();
		String statement = "SELECT education_level, total_hours*price_per_hour FROM Student "
				+ "INNER JOIN Payment ON Student.id=Payment.id_student "
				+ "INNER JOIN Lesson ON Payment.id_lesson=Lesson.id "
				+ "WHERE Payment.paid=1 AND Lesson.day BETWEEN ? AND ?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, DateParserSQLite.parseDate(beginDate));
			st.setString(2, DateParserSQLite.parseDate(endDate));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				EducationLevel level = EducationLevel.valueOf(rs.getString(1));
				Double total = rs.getDouble(2);
				if(inc.containsKey(level)) {
					inc.put(level, inc.get(level) + total);
				}
				else {
					inc.put(level, total);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TeacherSQLException("Hubo un error al intentar obtener el total de ganancias", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
		return inc;
	}
	@Override
	public Map<LocalDate, Double> getTotalIncomePerTime(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException {
		Map<LocalDate, Double> inc = new HashMap<>();
		String statement = "SELECT Lesson.day, total_hours*price_per_hour FROM Student "
				+ "INNER JOIN Payment ON Student.id=Payment.id_student "
				+ "INNER JOIN Lesson ON Payment.id_lesson=Lesson.id "
				+ "WHERE Payment.paid=1 AND Lesson.day BETWEEN ? AND ?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, DateParserSQLite.parseDate(beginDate));
			st.setString(2, DateParserSQLite.parseDate(endDate));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				LocalDate day = DateParserSQLite.parseString(rs.getString(1));
				Double total = rs.getDouble(2);
				if(inc.containsKey(day)) {
					inc.put(day, inc.get(day) + total);
				}
				else {
					inc.put(day, total);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TeacherSQLException("Hubo un error al intentar obtener el total de ganancias", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
		return inc;
	}
	@Override
	public Map<EducationLevel, Double> getTeacherIncomePerLevel(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException {
		Map<EducationLevel, Double> inc = new HashMap<>();
		String statement = "SELECT Commission.total, "
				+ "(SELECT Student.education_level FROM Student "
				+ "INNER JOIN Payment ON Student.id=Payment.id_student "
				+ "WHERE Payment.id_lesson=Lesson.id LIMIT 1) "
				+ "FROM Commission INNER JOIN Lesson ON Lesson.id=Commission.id_lesson "
				+ "WHERE Commission.paid=1 AND Lesson.day BETWEEN ? AND ? AND Commission.id_teacher!=1";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, DateParserSQLite.parseDate(beginDate));
			st.setString(2, DateParserSQLite.parseDate(endDate));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Double total = rs.getDouble(1);
				EducationLevel level = EducationLevel.valueOf(rs.getString(2));
				if(inc.containsKey(level)) {
					inc.put(level, inc.get(level) + total);
				}
				else {
					inc.put(level, total);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TeacherSQLException("Hubo un error al intentar obtener el total de ganancias de los profesores", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
		return inc;
	}
	@Override
	public Map<LocalDate, Double> getTeacherIncomePerTime(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException {
		Map<LocalDate, Double> inc = new HashMap<>();
		String statement = "SELECT Commission.total, Lesson.day "
				+ "FROM Commission INNER JOIN Lesson ON Lesson.id=Commission.id_lesson "
				+ "WHERE Commission.paid=1 AND Lesson.day BETWEEN ? AND ? AND Commission.id_teacher!=1";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, DateParserSQLite.parseDate(beginDate));
			st.setString(2, DateParserSQLite.parseDate(endDate));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Double total = rs.getDouble(1);
				LocalDate day = DateParserSQLite.parseString(rs.getString(2));
				if(inc.containsKey(day)) {
					inc.put(day, inc.get(day) + total);
				}
				else {
					inc.put(day, total);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TeacherSQLException("Hubo un error al intentar obtener el total de ganancias de los profesores", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
		return inc;
	}
	
	public static void main(String[] args) throws SQLException {
		Connection conn = ConnectionSQLite.connect();
		System.out.println(conn.isClosed());
		//Teacher teacher = new Teacher("Holas", "Chasau", LocalDate.now(), "223");
		//Teacher teacher2 = new Teacher("Holas", "Chasau", LocalDate.now(), "123");
		TeacherDao teacherDao = new TeacherDaoSQLite();
		// teacherDao.createTeacher(teacher);
		// teacherDao.modifyTeacher(teacher2);
		// teacherDao.deleteTeacher(teacher);
		teacherDao.getAllTeachers();
	}
}
