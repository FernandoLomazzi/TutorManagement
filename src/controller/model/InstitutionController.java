package controller.model;

import java.util.List;

import model.Institution;
import model.Subject;
import model.dao.InstitutionDao;
import model.dao.SubjectDao;
import model.dao.sqlite.InstitutionDaoSQLite;
import model.dao.sqlite.SubjectDaoSQLite;

public class InstitutionController {
	private static InstitutionController controller;
	
	private InstitutionController() {
		
	}
	public static InstitutionController getInstance() {
		if(controller==null) {
			controller = new InstitutionController();
		}
		return controller;
	}
	public Institution createInstitution(String name) {
		Institution inst = new Institution(name);
		InstitutionDao institutionDao = new InstitutionDaoSQLite();
		institutionDao.createInstitution(inst);
		return inst;
	}
	public void deleteInstitution(Institution inst) {
		InstitutionDao institutionDao = new InstitutionDaoSQLite();
		institutionDao.deleteInstitution(inst);
	}
	public List<Institution> getAllSubjects() {
		InstitutionDao institutionDao = new InstitutionDaoSQLite();
		return institutionDao.getAllInstitutions();
	}
}
