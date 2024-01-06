package model.dao.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Lesson;
import model.Payment;
import model.StudentReport;
import model.dao.LessonDao;

public class LessonDaoSQLite implements LessonDao{
	public void setNotified(StudentReport student, Boolean notified) {
		String statement = "UPDATE Payment SET is_notified=? WHERE Payment.id_student="
				+ "(SELECT id FROM Student WHERE Student.name=? AND Student.surname=?) AND Payment.id_lesson=?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setBoolean(1, notified);
			st.setString(2, student.getStudentName());
			st.setString(3, student.getStudentSurname());
			st.setInt(4, student.getLessonID());
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
	public void makePayment(StudentReport student) {
		//String statement = "UPDATE Payment SET Payment.paid=1 AND Payment.notified=1 WHERE Payment.id_student=() AND "
		String statement = "UPDATE Payment SET paid=1 WHERE "
				+ "Payment.id_student=(SELECT id FROM Student WHERE Student.name=? AND Student.surname=?) "
				+ "AND Payment.id_lesson=?;";
		
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, student.getStudentName());
			st.setString(2, student.getStudentSurname());
			st.setInt(3, student.getLessonID());
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
	public void createLesson(Lesson lesson) {
		String statementLesson = "INSERT INTO Lesson (id, total_hours, day, price_per_hour, state, id_subject) "
				+ "VALUES (?, ?, ?, ?, ?, (SELECT id FROM Subject WHERE name=?))";
		String statementCommission = "INSERT INTO Commission (id_lesson, id_teacher, price_per_hour, total, paid) "
				+ "VALUES (?, (SELECT id FROM Teacher WHERE name=? AND surname=?), ?, ?, ?)";
		String statementPayment = "INSERT INTO Payment (id_student, id_lesson, paid, is_notified)"
				+ "VALUES ((SELECT id FROM Student WHERE name=? AND surname=?), ?, ?, ?)";
		Connection conn = ConnectionSQLite.connect();
		try {
			conn.setAutoCommit(false);
			PreparedStatement stLesson = conn.prepareStatement(statementLesson);
			stLesson.setInt(1, lesson.getId());
			stLesson.setDouble(2, lesson.getTotalHours());
			stLesson.setString(3, DateParserSQLite.parseDate(lesson.getDay()));
			stLesson.setDouble(4, lesson.getPricePerHour());
			stLesson.setString(5, lesson.getState().toString());
			stLesson.setString(6, lesson.getSubject().getName());
			stLesson.execute();
			PreparedStatement stComm = conn.prepareStatement(statementCommission);
			stComm.setInt(1, lesson.getId());
			stComm.setString(2, lesson.getTeacher().getName());
			stComm.setString(3, lesson.getTeacher().getSurname());
			stComm.setDouble(4, lesson.getCommission().getPricePerHour());
			stComm.setDouble(5, lesson.getCommission().getTotal());
			stComm.setBoolean(6, lesson.getCommission().isPaid());
			stComm.execute();
			for(Payment payments: lesson.getPayments()) {
				PreparedStatement stPay = conn.prepareStatement(statementPayment);
				stPay.setString(1, payments.getStudent().getName());
				stPay.setString(2, payments.getStudent().getSurname());
				stPay.setInt(3, lesson.getId());
				stPay.setBoolean(4,  payments.isPaid());
				stPay.setBoolean(5,  payments.isPaid());
				stPay.execute();
			}
			conn.commit();
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
	public void deleteLesson(Lesson lesson) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getLessonID() {
		//add returns on catchs
		Integer max_id = null;
		String statement = "SELECT MAX(id) FROM Lesson";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			rs.next();
			max_id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return max_id+1;
	}
}
