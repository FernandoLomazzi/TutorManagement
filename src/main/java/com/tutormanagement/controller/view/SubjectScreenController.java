package com.tutormanagement.controller.view;

import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;

import com.tutormanagement.controller.model.InstitutionController;
import com.tutormanagement.controller.model.SubjectController;
import com.tutormanagement.model.Institution;
import com.tutormanagement.model.Subject;
import com.tutormanagement.utils.AlertManager;
import com.tutormanagement.utils.MaterialFXManager;
import com.tutormanagement.utils.StringManager;
import com.tutormanagement.utils.ValidatorManager;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
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
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class SubjectScreenController implements Initializable {
	@FXML
	private GridPane gridPane;
	@FXML
	private MFXTableView<Institution> instTable;
	@FXML
	private MFXTableView<Subject> subTable;
	@FXML
	private MFXTextField instNameField;
	@FXML
	private MFXButton addInstButton;
	@FXML
	private MFXButton deleteInstButton;
	@FXML
	private MFXTextField subNameField;
	@FXML
	private MFXFilterComboBox<Institution> subInstField;
	@FXML
	private MFXButton addSubButton;
	@FXML
	private MFXButton deleteSubButton;

	private ObservableList<Institution> institutions;
	private ObservableList<Subject> subjects;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			InstitutionController institutionController = InstitutionController.getInstance();
			institutions = FXCollections.observableArrayList(institutionController.getAllSubjects());
			SubjectController subjectController = SubjectController.getInstance();
			subjects = FXCollections.observableArrayList(subjectController.getAllSubjects());
		} catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
		}
		subTable.setItems(subjects);
		instTable.setItems(institutions);
		subInstField.setItems(institutions);
		setupFields();
		setupTable();
		subTable.autosizeColumnsOnInitialization();
		instTable.autosizeColumnsOnInitialization();
		When.onChanged(subTable.tableRowFactoryProperty()).then((o, n) -> subTable.autosizeColumns()).listen();
		When.onChanged(instTable.tableRowFactoryProperty()).then((o, n) -> instTable.autosizeColumns()).listen();
	}

	private void setupFields() {
		ValidatorManager.notNullConstraint(instNameField);
		ValidatorManager.notNullConstraint(subNameField);
		ValidatorManager.notNullConstraint(subInstField);
	}

	private void setupTable() {
		MFXTableColumn<Institution> instNameColumn = new MFXTableColumn<>("Nombre", true,
				Comparator.comparing(Institution::getName));
		MFXTableColumn<Subject> subNameColumn = new MFXTableColumn<>("Nombre", true,
				Comparator.comparing(Subject::getName));
		MFXTableColumn<Subject> subInstColumn = new MFXTableColumn<>("Institución", true,
				Comparator.comparing(Subject::getInstitutionString));
		instNameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Institution::getName));
		subNameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Subject::getName));
		subInstColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Subject::getInstitution));

		instTable.getTableColumns().addAll(instNameColumn);
		subTable.getTableColumns().addAll(subNameColumn, subInstColumn);

		instTable.getFilters().addAll(new StringFilter<>("Nombre", Institution::getName));
		subTable.getFilters().addAll(new StringFilter<>("Nombre", Subject::getName),
				new StringFilter<>("Institución", Subject::getInstitutionString));
	}

	@FXML
	public void createInstitution(ActionEvent event) {
		String name = StringManager.capitalize(instNameField.getText().trim());
		if (instNameField.validate().isEmpty()) {
			InstitutionController institutionController = InstitutionController.getInstance();
			try {
				Institution newInst = institutionController.createInstitution(name);
				institutions.add(newInst);

				MaterialFXManager.clearAllFields(instNameField);

				AlertManager.createInformation("Éxito", "La institución se ha creado exitosamente", gridPane);
			} catch (SQLException e) {
				AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
			}
		} else {
			AlertManager.createError("Error",
					"Complete todos los campos obligatorios antes de agregar una nueva institución", gridPane);
		}
	}

	@FXML
	public void deleteInstitution(ActionEvent eve) {
		Institution selectedInst = instTable.getSelectionModel().getSelectedValue();
		if (selectedInst != null) {
			Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado",
					"¿Desea eliminar esta institución del sistema? Una vez realizado será irrevertible.", gridPane);
			alert.getKey().addActions(Map.entry(new MFXButton("Confirmar"), event -> {
				alert.getValue().close();
				try {
					InstitutionController institutionController = InstitutionController.getInstance();
					institutionController.deleteInstitution(selectedInst);
					institutions.remove(selectedInst);
				} catch (SQLException e) {
					AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
				}
			}), Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close()));
			alert.getValue().showDialog();
		} else {
			AlertManager.createError("Error", "Debe seleccionar a una institución antes de eliminarla", gridPane);
		}
	}

	// Event Listener on MFXButton[#addSubButton].onAction
	@FXML
	public void createSubject(ActionEvent event) {
		String name = StringManager.capitalize(subNameField.getText().trim());
		Institution inst = subInstField.getSelectedItem();
		if (subNameField.validate().isEmpty() && subInstField.validate().isEmpty()) {
			SubjectController subjectController = SubjectController.getInstance();
			try {
				Subject subject = subjectController.createSubject(name, inst);
				subjects.add(subject);

				MaterialFXManager.clearAllFields(subNameField);

				AlertManager.createInformation("Éxito", "La materia se ha creado exitosamente", gridPane);
			} catch (SQLException e) {
				AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
			}

		} else {
			AlertManager.createError("Error",
					"Complete todos los campos obligatorios antes de agregar una nueva materia", gridPane);
		}
	}

	// Event Listener on MFXButton[#deleteSubButton].onAction
	@FXML
	public void deleteSubject(ActionEvent eve) {
		Subject selectedSubject = subTable.getSelectionModel().getSelectedValue();
		if (selectedSubject != null) {
			Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado",
					"¿Desea eliminar esta materia del sistema? Una vez realizado será irrevertible.", gridPane);
			alert.getKey().addActions(Map.entry(new MFXButton("Confirmar"), event -> {
				alert.getValue().close();
				try {
					SubjectController subjectController = SubjectController.getInstance();
					subjectController.deleteSubject(selectedSubject);
					subjects.remove(selectedSubject);
				} catch (SQLException e) {
					AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
				}

			}), Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close()));
			alert.getValue().showDialog();
		} else {
			AlertManager.createError("Error", "Debe seleccionar a una materia antes de eliminarla", gridPane);
		}
	}
}
