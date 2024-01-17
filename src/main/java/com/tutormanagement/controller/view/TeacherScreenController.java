package com.tutormanagement.controller.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;

import com.tutormanagement.controller.model.TeacherController;
import com.tutormanagement.model.Teacher;
import com.tutormanagement.utils.AlertManager;
import com.tutormanagement.utils.MaterialFXManager;
import com.tutormanagement.utils.StringManager;
import com.tutormanagement.utils.ValidatorManager;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;

public class TeacherScreenController implements Initializable {
	@FXML
	private MFXButton addButton;
	@FXML
	private MFXDatePicker birthdayField;
	@FXML
	private MFXButton deleteButton;
	@FXML
	private TextArea descriptionField;
	@FXML
	private GridPane gridPane;
	@FXML
	private MFXTextField nameField;
	@FXML
	private MFXTextField surnameField;
	@FXML
	private MFXTableView<Teacher> teacherTable;

	private ObservableList<Teacher> teachers;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupFields();
		setupTable();
		teacherTable.autosizeColumnsOnInitialization();
		When.onChanged(teacherTable.tableRowFactoryProperty()).then((o, n) -> teacherTable.autosizeColumns()).listen();
		TeacherController teacherController = TeacherController.getInstance();
		try {
			teachers = FXCollections.observableArrayList(teacherController.getAllTeachers());
			teacherTable.setItems(teachers);
		} catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
		}
	}

	private void setupFields() {
		ValidatorManager.notNullConstraint(nameField);
		ValidatorManager.notNullConstraint(surnameField);
		ValidatorManager.notNullConstraint(birthdayField);
	}

	private void setupTable() {
		MFXTableColumn<Teacher> nameColumn = new MFXTableColumn<>("Nombre", true,
				Comparator.comparing(Teacher::getName));
		MFXTableColumn<Teacher> surnameColumn = new MFXTableColumn<>("Apellido", true,
				Comparator.comparing(Teacher::getSurname));
		MFXTableColumn<Teacher> birthdayColumn = new MFXTableColumn<>("Cumpleaños", true,
				Comparator.comparing(Teacher::getBirthday));
		MFXTableColumn<Teacher> descriptionColumn = new MFXTableColumn<>("Descripción", true,
				Comparator.comparing(Teacher::getDescription));

		nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Teacher::getName));
		surnameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Teacher::getSurname));
		birthdayColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Teacher::getBirthday));
		descriptionColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Teacher::getDescription));

		teacherTable.getTableColumns().addAll(nameColumn, surnameColumn, birthdayColumn, descriptionColumn);

		teacherTable.getFilters().addAll(new StringFilter<>("Nombre", Teacher::getName),
				new StringFilter<>("Apellido", Teacher::getSurname));
	}

	@FXML
	void generateReport(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TeacherReportScreen.fxml"));
		try {
			Pane root = loader.load();
			((StackPane) gridPane.getParent()).getChildren().setAll(root);
		} catch (IOException e) {
			AlertManager.createError("ERROR",
					"Hubo un error al intentar cargar el reporte de profesores\n" + e.getMessage(), gridPane);
		}
	}

	private Boolean checkFieldContraints() {
		return nameField.isValid() && surnameField.isValid() && birthdayField.isValid();
	}

	@FXML
	void addTeacher(ActionEvent event) {
		String name = StringManager.capitalize(nameField.getText().trim());
		String surname = StringManager.capitalize(surnameField.getText().trim());
		LocalDate birthday = birthdayField.getValue();
		String description = StringManager.capitalize(descriptionField.getText().trim());
		if (checkFieldContraints()) {
			TeacherController teacherController = TeacherController.getInstance();
			try {
				Teacher newTeacher = teacherController.createTeacher(name, surname, birthday, description);
				teachers.add(newTeacher);
				MaterialFXManager.clearAllFields(nameField, surnameField, birthdayField);
				descriptionField.clear();
				AlertManager.createInformation("Éxito", "El profesor se ha creado exitosamente", gridPane);
			} catch (SQLException e) {
				AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
			}
		} else {
			AlertManager.createError("Error",
					"Complete todos los campos obligatorios antes de agregar un nuevo profesor", gridPane);
		}
	}

	@FXML
	void deleteTeacher(ActionEvent event) {
		Teacher selectedTeacher = teacherTable.getSelectionModel().getSelectedValue();
		if (selectedTeacher != null) {
			Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado",
					"¿Desea eliminar este profesor del sistema? Una vez realizado será irrevertible.", gridPane);
			alert.getKey().addActions(Map.entry(new MFXButton("Confirmar"), e -> {
				alert.getValue().close();
				TeacherController teacherController = TeacherController.getInstance();
				try {
					teacherController.deleteTeacher(selectedTeacher);
					teachers.remove(selectedTeacher);
				} catch (SQLException ex) {
					AlertManager.createError("ERROR " + ex.getErrorCode(), ex.getMessage(), gridPane);
				}
			}), Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close()));
			alert.getValue().showDialog();
		} else {
			AlertManager.createError("Error", "Debe seleccionar a un profesor antes de eliminarlo", gridPane);
		}
	}
}
