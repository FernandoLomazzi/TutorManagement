package com.tutormanagement.controller.view;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

import com.tutormanagement.controller.model.LessonController;
import com.tutormanagement.model.TeacherReport;
import com.tutormanagement.utils.AlertManager;
import com.tutormanagement.utils.ValidatorManager;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class ModifyLessonScreenController implements Initializable{
	@FXML
	private GridPane gridPane;
	@FXML
	private MFXListView<String> studentList;
	@FXML
	private MFXTextField totalHoursField;
	@FXML
	private MFXTextField subjectField;
	@FXML
	private MFXTextField idField;
	@FXML
	private MFXTextField pricePerHourField;
	@FXML
	private MFXTextField teacherField;
	@FXML
	private MFXTextField pricePerHourStudentField;
	@FXML
	private MFXButton acceptButton;
	@FXML
	private MFXButton cancelButton;
	@FXML
	private MFXDatePicker dateField;
	
	private ObservableList<String> students;
	private TeacherReport lesson;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ValidatorManager.notNullConstraint(pricePerHourField);
		ValidatorManager.notNullConstraint(totalHoursField);
		ValidatorManager.notNullConstraint(dateField);
		ValidatorManager.notNullConstraint(pricePerHourStudentField);
	}

	public void initializeFields(TeacherReport l) {
		lesson = l;
		if(lesson.getTeacherFullName().equals("Ingres Ar")) {
			pricePerHourField.setEditable(false);
		}
		idField.setText(lesson.getLessonID().toString());
		subjectField.setText(lesson.getSubjectName());
		dateField.setValue(lesson.getDay());
		teacherField.setText(lesson.getTeacherFullName());
		pricePerHourField.setText(lesson.getTeacherHourPrice().toString());
		totalHoursField.setText(lesson.getTotalHours().toString());
		pricePerHourStudentField.setText(lesson.getStudentHourPrice().toString());
		this.students = FXCollections.observableArrayList(lesson.getStudents());
		studentList.setItems(this.students);
	}
	
	@FXML
	public void accept(ActionEvent ev) {
		Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Confirmación",
				"¿Desea guardar los cambios realizados a esta clase en el sistema? Una vez realizado será irrevertible.", gridPane);
		alert.getKey().addActions(Map.entry(new MFXButton("Confirmar"), event -> {
			alert.getValue().close();
			if (!(pricePerHourField.isValid() && totalHoursField.isValid() && dateField.isValid() && pricePerHourStudentField.isValid())) {
				AlertManager.createError("Error", "No puede dejar campos obligatorios vacíos al modificar la clase",
						gridPane);
				return;
			}
			LocalDate day = dateField.getValue();
			Double totalHours;
			Double pricePerHour;
			Double pricePerHourStudent;
			try {
				totalHours = Double.parseDouble(totalHoursField.getText().trim());
				pricePerHour = Double.parseDouble(pricePerHourField.getText().trim());
				pricePerHourStudent = Double.parseDouble(pricePerHourStudentField.getText().trim());
			} catch (Exception e) {
				AlertManager.createError("Error", "Complete los campos numéricos con números válidos", gridPane);
				return;
			}
			try {
				lesson.modifyValues(day, totalHours, pricePerHourStudent, pricePerHour, students.size());
				LessonController lessonController = LessonController.getInstance();
				lessonController.modifyLesson(lesson);
				((Stage) gridPane.getScene().getWindow()).close();
			} catch (SQLException e) {
				AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
			}
		}), Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close()));
		alert.getValue().showDialog();
	}
	
	@FXML
	public void cancel(ActionEvent event) {
		((Stage) gridPane.getScene().getWindow()).close();
	}



}
