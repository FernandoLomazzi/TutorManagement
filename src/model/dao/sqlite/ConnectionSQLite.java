package model.dao.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionSQLite {
	private static String url = "jdbc:sqlite:src/database.db"; 
	public static Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		}catch(SQLException e) {
	        System.out.println(e.getMessage());
		}
		return conn;		
	}
	public static void main(String[] args) throws SQLException {
		Connection conn = ConnectionSQLite.connect();
		System.out.println(conn.isClosed());
		try {
			Statement st = conn.createStatement();
			st.execute("insert into Institution (name) values ('asd')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
