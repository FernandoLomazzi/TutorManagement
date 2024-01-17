package com.tutormanagement.controller.model;

import java.util.List;

import com.tutormanagement.model.Institution;
import com.tutormanagement.model.dao.InstitutionDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.InstitutionSQLException;
import com.tutormanagement.model.dao.sqlite.InstitutionDaoSQLite;

public class InstitutionController {
	private static InstitutionController controller;

	private InstitutionController() {

	}

	public static InstitutionController getInstance() {
		if (controller == null) {
			controller = new InstitutionController();
		}
		return controller;
	}

	public Institution createInstitution(String name) throws ConnectionException, InstitutionSQLException {
		Institution inst = new Institution(name);
		InstitutionDao institutionDao = new InstitutionDaoSQLite();
		institutionDao.createInstitution(inst);
		return inst;
	}

	public void deleteInstitution(Institution inst) throws ConnectionException, InstitutionSQLException {
		InstitutionDao institutionDao = new InstitutionDaoSQLite();
		institutionDao.deleteInstitution(inst);
	}

	public List<Institution> getAllSubjects() throws ConnectionException, InstitutionSQLException {
		InstitutionDao institutionDao = new InstitutionDaoSQLite();
		return institutionDao.getAllInstitutions();
	}
}
