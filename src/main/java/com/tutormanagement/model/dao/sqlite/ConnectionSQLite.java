package com.tutormanagement.model.dao.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.tutormanagement.model.dao.exception.ConnectionException;

public class ConnectionSQLite {
	private ConnectionSQLite() {
		;
	}
	//private static String dbUrl = new File(getClass().getResource("/database/database.db").getPath()).getAbsolutePath();
	private static String dbUrl = "database/database.db";
	private static String url = "jdbc:sqlite:"+dbUrl+"?foreign_keys=on";

	public static Connection connect() throws ConnectionException {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ConnectionException("Error al establecer una conexión con la base de datos.", e);
		}
		return conn;
	}

	public static void disconnect(Connection conn) throws ConnectionException {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			throw new ConnectionException("Error al cerrar la conexión a la base de datos", e);
		}
	}

	public static void main(String[] args) throws SQLException {
		try {
			Connection conn = ConnectionSQLite.connect();
			System.out.println(conn.isClosed());
			Statement st = conn.createStatement();
			st.execute("insert into Institution (name) values ('asd')");
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
