package com.tutormanagement.model.dao.exception;

import java.sql.SQLException;

public class SubjectSQLException extends SQLException {
	public SubjectSQLException(String message, Throwable cause) {
		super(message, cause);
	}
}
