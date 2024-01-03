package model.dao.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Institution;
import model.dao.InstitutionDao;

public class InstitutionDaoSQLite implements InstitutionDao{

	@Override
	public void createInstitution(Institution institution) {
		String statement = "INSERT INTO Institution (name) VALUES (?)";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, institution.getName());
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
	public void deleteInstitution(Institution institution) {
		String statement = "DELETE FROM Institution WHERE name=?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, institution.getName());
			st.execute();
		} catch (SQLException e) {
			System.out.println("HOLA");
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("HOLA2");
			}
		}
	}

	@Override
	public List<Institution> getAllInstitutions() {
		List<Institution> institutions = new ArrayList<>();
		String statement = "SELECT name FROM Institution";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				String name;
				name = rs.getString(1);
				Institution teacher = new Institution(name);
				institutions.add(teacher);
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
		System.out.println(institutions);
		return institutions;
	}

}
