package com.tutormanagement.model.dao.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tutormanagement.model.Institution;
import com.tutormanagement.model.Subject;
import com.tutormanagement.model.dao.SubjectDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.SubjectSQLException;

public class SubjectDaoSQLite implements SubjectDao {

	@Override
	public void createSubject(Subject subject) throws ConnectionException, SubjectSQLException {
		String statement = "INSERT INTO Subject (name, id_institution)"
				+ "VALUES (?, (SELECT id FROM Institution WHERE name=?))";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, subject.getName());
			st.setString(2, subject.getInstitution().getName());
			st.execute();
		} catch (SQLException e) {
			throw new SubjectSQLException("Hubo un error al intentar crear la materia", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
	}

	@Override
	public void deleteSubject(Subject subject) throws ConnectionException, SubjectSQLException {
		String statement = "DELETE FROM Subject WHERE name=?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, subject.getName());
			st.execute();
		} catch (SQLException e) {
			throw new SubjectSQLException("Hubo un error al intentar eliminar la materia", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
	}

	@Override
	public List<Subject> getAllSubjects() throws ConnectionException, SubjectSQLException {
		List<Subject> subjects = new ArrayList<>();
		Connection conn = ConnectionSQLite.connect();
		String statement = "SELECT Subject.name,Institution.name FROM Subject INNER JOIN Institution ON Subject.id_institution=Institution.id";
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				String subjectName, instName;
				subjectName = rs.getString(1);
				instName = rs.getString(2);
				Institution inst = new Institution(instName);
				Subject subject = new Subject(subjectName, inst);
				subjects.add(subject);
			}
		} catch (SQLException e) {
			throw new SubjectSQLException("Hubo un error al intentar obtener todas las materias", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
		return subjects;
	}

}
