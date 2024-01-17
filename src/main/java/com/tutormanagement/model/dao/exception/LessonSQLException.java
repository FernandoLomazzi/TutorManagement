package com.tutormanagement.model.dao.exception;

import java.sql.SQLException;

public class LessonSQLException extends SQLException {
	public LessonSQLException(String message, Throwable cause) {
		super(message, cause);
	}
}
