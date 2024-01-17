package com.tutormanagement.controller.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;

import com.tutormanagement.controller.model.StudentController;
import com.tutormanagement.model.EducationLevel;
import com.tutormanagement.model.Student;
import com.tutormanagement.utils.AlertManager;
import com.tutormanagement.utils.MaterialFXManager;
import com.tutormanagement.utils.StringManager;
import com.tutormanagement.utils.ValidatorManager;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.filter.EnumFilter;
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

public class StudentScreenController implements Initializable {
	@FXML
	private GridPane gridPane;
	@FXML
	private MFXButton addButton;
	@FXML
	private MFXTextField addressField;
	@FXML
	private MFXTextField phoneNumberField;
	@FXML
	private MFXDatePicker birthdayField;
	@FXML
	private MFXButton deleteButton;
	@FXML
	private TextArea descriptionField;
	@FXML
	private MFXTextField nameField;
	@FXML
	private MFXComboBox<EducationLevel> edLevelField;
	@FXML
	private MFXTextField socialMediaField;
	@FXML
	private MFXTableView<Student> studentTable;
	@FXML
	private MFXTextField surnameField;
	@FXML
	private MFXButton reportButton;

	private ObservableList<Student> students;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupFields();
		setupTable();
		studentTable.autosizeColumnsOnInitialization();
		When.onChanged(studentTable.tableRowFactoryProperty()).then((o, n) -> studentTable.autosizeColumns()).listen();
		StudentController studentController = StudentController.getInstance();
		try {
			students = FXCollections.observableArrayList(studentController.getAllStudents());
			studentTable.setItems(students);
		} catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
		}
	}

	private void setupFields() {
		edLevelField.setItems(FXCollections.observableArrayList(EducationLevel.values()));
		edLevelField.selectFirst();
		ValidatorManager.notNullConstraint(nameField);
		ValidatorManager.notNullConstraint(surnameField);
		ValidatorManager.notNullConstraint(phoneNumberField);
		ValidatorManager.notNullConstraint(birthdayField);
		ValidatorManager.notNullConstraint(edLevelField);
	}

	private void setupTable() {
		MFXTableColumn<Student> nameColumn = new MFXTableColumn<>("Nombre", true,
				Comparator.comparing(Student::getName));
		MFXTableColumn<Student> surnameColumn = new MFXTableColumn<>("Apellido", true,
				Comparator.comparing(Student::getSurname));
		MFXTableColumn<Student> addressColumn = new MFXTableColumn<>("Dirección", true,
				Comparator.comparing(Student::getAddress));
		MFXTableColumn<Student> phoneNumberColumn = new MFXTableColumn<>("Número", true,
				Comparator.comparing(Student::getPhoneNumber));
		MFXTableColumn<Student> birthdayColumn = new MFXTableColumn<>("Cumpleaños", true,
				Comparator.comparing(Student::getBirthday));
		MFXTableColumn<Student> socialMediaColumn = new MFXTableColumn<>("Red social", true,
				Comparator.comparing(Student::getSocialMedia));
		MFXTableColumn<Student> levelColumn = new MFXTableColumn<>("Nivel", true,
				Comparator.comparing(Student::getEducationLevel));

		nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getName));
		surnameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getSurname));
		addressColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getAddress));
		phoneNumberColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getPhoneNumber));
		birthdayColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getBirthday));
		socialMediaColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getSocialMedia));
		levelColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getEducationLevelInitial));

		studentTable.getTableColumns().addAll(nameColumn, surnameColumn, addressColumn, phoneNumberColumn,
				birthdayColumn, socialMediaColumn, levelColumn);

		studentTable.getFilters().addAll(new StringFilter<>("Nombre", Student::getName),
				new StringFilter<>("Apellido", Student::getSurname),
				new EnumFilter<>("Nivel", Student::getEducationLevel, EducationLevel.class));
	}

	@FXML
	void generateReport(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudentReportScreen.fxml"));
		try {
			Pane root = loader.load();
			((StackPane) gridPane.getParent()).getChildren().setAll(root);
		} catch (IOException e) {
			AlertManager.createError("ERROR",
					"Hubo un error al intentar cargar el reporte de estudiantes\n" + e.getMessage(), gridPane);
		}
	}

	private Boolean checkFieldConstraints() {
		return nameField.isValid() && surnameField.isValid() && phoneNumberField.isValid() && birthdayField.isValid()
				&& edLevelField.isValid();
	}

	@FXML
	void addStudent(ActionEvent event) {
		String name = StringManager.capitalize(nameField.getText().trim());
		String surname = StringManager.capitalize(surnameField.getText().trim());
		String address = StringManager.capitalize(addressField.getText().trim());
		String phoneNumber = phoneNumberField.getText().trim();
		LocalDate birthday = birthdayField.getValue();
		String socialMedia = StringManager.capitalize(socialMediaField.getText().trim());
		String description = StringManager.capitalize(descriptionField.getText().trim());
		EducationLevel lvl = edLevelField.getSelectedItem();

		if (checkFieldConstraints()) {
			StudentController studentController = StudentController.getInstance();
			try {
				Student newStudent = studentController.createStudent(name, surname, address, phoneNumber, birthday,
						socialMedia, description, lvl);
				students.add(newStudent);

				MaterialFXManager.clearAllFields(nameField, surnameField, addressField, phoneNumberField, birthdayField,
						socialMediaField);
				descriptionField.clear();

				AlertManager.createInformation("Éxito", "El estudiante se ha creado exitosamente", gridPane);
			} catch (SQLException e) {
				AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
			}
		} else {
			AlertManager.createError("Error", "Complete todos los campos obligatorios antes de agregar un nuevo alumno",
					gridPane);
		}
	}

	@FXML
	void deleteStudent(ActionEvent ev) {
		Student selectedStudent = studentTable.getSelectionModel().getSelectedValue();
		if (selectedStudent != null) {
			Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado",
					"¿Desea eliminar este estudiante del sistema? Una vez realizado será irrevertible.", gridPane);
			alert.getKey().addActions(Map.entry(new MFXButton("Confirmar"), event -> {
				alert.getValue().close();
				StudentController studentController = StudentController.getInstance();
				try {
					studentController.deleteStudent(selectedStudent);
					students.remove(selectedStudent);
				} catch (SQLException e) {
					AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
				}
			}), Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close()));
			alert.getValue().showDialog();
		} else {
			AlertManager.createError("Error", "Debe seleccionar a un estudiante antes de eliminarlo", gridPane);
		}
	}

}