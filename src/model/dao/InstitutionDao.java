package model.dao;

import java.util.List;

import model.Institution;
import model.Teacher;

public interface InstitutionDao {
	public void createInstitution(Institution institution);
	public void deleteInstitution(Institution institution);
	public List<Institution> getAllInstitutions();
}
