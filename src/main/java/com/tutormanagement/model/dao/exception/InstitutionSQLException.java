package com.tutormanagement.model.dao.exception;

import java.sql.SQLException;

public class InstitutionSQLException extends SQLException {
	public InstitutionSQLException(String message, Throwable cause) {
		super(message, cause);
	}
}
