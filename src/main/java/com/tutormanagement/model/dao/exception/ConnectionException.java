package com.tutormanagement.model.dao.exception;

import java.sql.SQLException;

public class ConnectionException extends SQLException {
	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}
	/*
	 * public static void main(String[] args) { ConnectionException conn = new
	 * ConnectionException("HOLA", new Exception()); conn.printStackTrace(); }
	 */
}
