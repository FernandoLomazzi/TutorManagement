package com.tutormanagement.model.dao.exception;

import java.sql.SQLException;

public class StudentSQLException extends SQLException {
	public StudentSQLException(String message, Throwable cause) {
		super(message, cause);
	}
}
