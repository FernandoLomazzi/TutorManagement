package com.tutormanagement.model.dao.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tutormanagement.model.Institution;
import com.tutormanagement.model.dao.InstitutionDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.InstitutionSQLException;

public class InstitutionDaoSQLite implements InstitutionDao {

	@Override
	public void createInstitution(Institution institution) throws ConnectionException, InstitutionSQLException {
		String statement = "INSERT INTO Institution (name) VALUES (?)";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, institution.getName());
			st.execute();
		} catch (SQLException e) {
			throw new InstitutionSQLException("Hubo un error al intentar crear la institución", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
	}

	@Override
	public void deleteInstitution(Institution institution) throws ConnectionException, InstitutionSQLException {
		String statement = "DELETE FROM Institution WHERE name=?";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			st.setString(1, institution.getName());
			st.execute();
		} catch (SQLException e) {
			throw new InstitutionSQLException("Hubo un error al intentar eliminar la institución", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
	}

	@Override
	public List<Institution> getAllInstitutions() throws ConnectionException, InstitutionSQLException {
		List<Institution> institutions = new ArrayList<>();
		String statement = "SELECT name FROM Institution";
		Connection conn = ConnectionSQLite.connect();
		try {
			PreparedStatement st = conn.prepareStatement(statement);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				String name;
				name = rs.getString(1);
				Institution teacher = new Institution(name);
				institutions.add(teacher);
			}
		} catch (SQLException e) {
			throw new InstitutionSQLException("Hubo un error al intentar obtener todas las instituciones", e);
		} finally {
			ConnectionSQLite.disconnect(conn);
		}
		return institutions;
	}

}
