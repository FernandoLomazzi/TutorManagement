package model.dao.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Institution;
import model.Subject;
import model.dao.SubjectDao;

public class SubjectDaoSQLite implements SubjectDao {

	@Override
	public void createSubject(Subject subject) {
		String statement = "INSERT INTO Subject (name, id_institution)"
				+ "VALUES (?, (SELECT id FROM Institution WHERE name=?))";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, subject.getName());
			st.setString(2, subject.getInstitution().getName());
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
	public void deleteSubject(Subject subject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Subject> getAllSubjects() {
		List<Subject> subjects = new ArrayList<>();
		Connection conn = ConnectionSQLite.connect();
		String statement = "SELECT Subject.name,Institution.name FROM Subject INNER JOIN Institution ON Subject.id_institution=Institution.id";
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				String subjectName, instName;
				subjectName = rs.getString(1);
				instName = rs.getString(2);
				Institution inst = new Institution(instName);
				Subject subject = new Subject(subjectName, inst);
				subjects.add(subject);
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
		System.out.println(subjects);
		return subjects;
	}

}
