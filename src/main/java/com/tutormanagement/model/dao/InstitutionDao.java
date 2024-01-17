package com.tutormanagement.model.dao;

import java.util.List;

import com.tutormanagement.model.Institution;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.InstitutionSQLException;

public interface InstitutionDao {
	public void createInstitution(Institution institution) throws ConnectionException, InstitutionSQLException;

	public void deleteInstitution(Institution institution) throws ConnectionException, InstitutionSQLException;

	public List<Institution> getAllInstitutions() throws ConnectionException, InstitutionSQLException;
}
