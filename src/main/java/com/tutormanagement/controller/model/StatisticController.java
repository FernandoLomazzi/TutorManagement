package com.tutormanagement.controller.model;

import java.time.LocalDate;
import java.util.Map;

import com.tutormanagement.model.EducationLevel;
import com.tutormanagement.model.dao.TeacherDao;
import com.tutormanagement.model.dao.exception.ConnectionException;
import com.tutormanagement.model.dao.exception.TeacherSQLException;
import com.tutormanagement.model.dao.sqlite.TeacherDaoSQLite;

public class StatisticController {
	private static StatisticController controller;

	private StatisticController() {

	}

	public static StatisticController getInstance() {
		if (controller == null) {
			controller = new StatisticController();
		}
		return controller;
	}
	
	public Map<EducationLevel, Double> getTotalIncomePerLevel(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException {
		TeacherDao teacherDao = new TeacherDaoSQLite();
		return teacherDao.getTotalIncomePerLevel(beginDate, endDate);
	}
	public Map<EducationLevel, Double> getTeacherIncomePerLevel(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException {
		TeacherDao teacherDao = new TeacherDaoSQLite();
		return teacherDao.getTeacherIncomePerLevel(beginDate, endDate);
	}
	
	public Map<LocalDate, Double> getTotalIncomePerTime(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException {
		TeacherDao teacherDao = new TeacherDaoSQLite();
		return teacherDao.getTotalIncomePerTime(beginDate, endDate);
	}
	public Map<LocalDate, Double> getTeacherIncomePerTime(LocalDate beginDate, LocalDate endDate) throws ConnectionException, TeacherSQLException {
		TeacherDao teacherDao = new TeacherDaoSQLite();
		return teacherDao.getTeacherIncomePerTime(beginDate, endDate);
	}

}
