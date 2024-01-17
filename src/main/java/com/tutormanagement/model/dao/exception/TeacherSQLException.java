package com.tutormanagement.model.dao.exception;

import java.sql.SQLException;

public class TeacherSQLException extends SQLException {
	public TeacherSQLException(String message, Throwable cause) {
		super(message, cause);
	}
}
