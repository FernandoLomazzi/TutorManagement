package com.tutormanagement.model.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.tutormanagement.model.EducationLevel;
import com.tutormanagement.model.Teacher;
import com.tutormanagement.model.TeacherReport;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.TeacherSQLException;

public interface TeacherDao {
	public void createTeacher(Teacher teacher) throws ConnectionException, TeacherSQLException;

	// public void modifyTeacher(Teacher teacher);
	public void deleteTeacher(Teacher teacher) throws ConnectionException, TeacherSQLException;

	public List<Teacher> getAllTeachers() throws ConnectionException, TeacherSQLException;

	Map<String, List<TeacherReport>> getUnpaidTeachers() throws ConnectionException, TeacherSQLException;

	Map<EducationLevel, Double> getTotalIncomePerLevel(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException;

	Map<EducationLevel, Double> getTeacherIncomePerLevel(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException;

	Map<LocalDate, Double> getTotalIncomePerTime(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException;

	Map<LocalDate, Double> getTeacherIncomePerTime(LocalDate beginDate, LocalDate endDate)
			throws ConnectionException, TeacherSQLException;
}
