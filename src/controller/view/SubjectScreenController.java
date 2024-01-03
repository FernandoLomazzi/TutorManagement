package controller.view;

import java.net.URL;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;

import controller.AlertManager;
import controller.ValidatorManager;
import controller.model.InstitutionController;
import controller.model.SubjectController;
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
import model.Institution;
import model.Subject;

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
	
	public static ObservableList<Institution> institutions;
	public static ObservableList<Subject> subjects;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		InstitutionController institutionController = InstitutionController.getInstance();
		institutions = FXCollections.observableArrayList(institutionController.getAllSubjects());
		SubjectController subjectController = SubjectController.getInstance();
		subjects = FXCollections.observableArrayList(subjectController.getAllSubjects());	
		subTable.setItems(subjects);
		instTable.setItems(institutions);
		subInstField.setItems(institutions);
		setupFields();
		setupTable();		
		subTable.autosizeColumnsOnInitialization();
		instTable.autosizeColumnsOnInitialization();
		When.onChanged(subTable.tableRowFactoryProperty()).then((o, n) -> subTable.autosizeColumns()).listen();
		When.onChanged(instTable.tableRowFactoryProperty()).then((o, n) -> {
			instTable.autosizeColumns();
			System.out.println("AAA " + o + " " + n);
		}).listen();
		
	}
	private void setupFields() {			
		ValidatorManager.notNullConstraint(instNameField);
		ValidatorManager.notNullConstraint(subNameField);
		ValidatorManager.notNullConstraint(subInstField);
	}
	private void setupTable() {
		MFXTableColumn<Institution> instNameColumn = new MFXTableColumn<>("Nombre", true, Comparator.comparing(Institution::getName));
		MFXTableColumn<Subject> subNameColumn = new MFXTableColumn<>("Nombre", false, Comparator.comparing(Subject::getName));
		MFXTableColumn<Subject> subInstColumn = new MFXTableColumn<>("Institución", false, Comparator.comparing(Subject::getInstitutionString));
		instNameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Institution::getName));
		subNameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Subject::getName));
		subInstColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Subject::getInstitution));
		
		instTable.getTableColumns().addAll(instNameColumn);
		subTable.getTableColumns().addAll(subNameColumn, subInstColumn);

		instTable.getFilters().addAll(
				new StringFilter<>("Nombre", Institution::getName)
		);
		subTable.getFilters().addAll(
				new StringFilter<>("Nombre", Subject::getName),
				new StringFilter<>("Institución", Subject::getInstitutionString)
		);
	}
	@FXML
	public void createInstitution(ActionEvent event) {
		String name = instNameField.getText().trim();
    	if(instNameField.validate().isEmpty()) {
    		InstitutionController institutionController = InstitutionController.getInstance();
    		Institution newInst = institutionController.createInstitution(name);
    		institutions.add(newInst);
        	instNameField.clear();    		
        	instNameField.deselect();
        	
        	AlertManager.createInformation("Éxito", "La institución se ha creado exitosamente", gridPane);
    	}
    	else {
    		AlertManager.createError("Error" , "Debe completar el campo nombre", gridPane);
    	}
	}
	
	@FXML
	public void deleteInstitution(ActionEvent event) {
    	Institution selectedInst = instTable.getSelectionModel().getSelectedValue();
    	if(selectedInst != null) {
    		Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado", "¿Desea eliminar esta institución del sistema? Una vez realizado será irrevertible.", gridPane);
    		alert.getKey().addActions(
    				Map.entry(new MFXButton("Confirmar"), e -> {
    		    		InstitutionController institutionController = InstitutionController.getInstance();
    		    		institutionController.deleteInstitution(selectedInst);
    					institutions.remove(selectedInst);
    					alert.getValue().close();
    				}),
    				Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close())
    		);
    		alert.getValue().showDialog();
    	}
    	else {
    		AlertManager.createError("Error", "Debe seleccionar a una institución antes de eliminarla", gridPane);
    	}
	}
	// Event Listener on MFXButton[#addSubButton].onAction
	@FXML
	public void createSubject(ActionEvent event) {
		String name = subNameField.getText().trim();
		Institution inst = subInstField.getSelectedItem();
    	if(subNameField.validate().isEmpty() && subInstField.validate().isEmpty()) {
    		SubjectController subjectController = SubjectController.getInstance();
    		Subject subject = subjectController.createSubject(name, inst);
    		subjects.add(subject);
        	subNameField.clear();
        	subNameField.deselect();
        	//subInstField.clearSelection();
        	AlertManager.createInformation("Éxito", "La materia se ha creado exitosamente", gridPane);
    	}
    	else {
    		AlertManager.createError("Error" , "Debe completar el campo nombre e institución", gridPane);
    	}
	}
	// Event Listener on MFXButton[#deleteSubButton].onAction
	@FXML
	public void deleteSubject(ActionEvent event) {
    	Subject selectedSubject = subTable.getSelectionModel().getSelectedValue();
    	if(selectedSubject != null) {
    		Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado", "¿Desea eliminar esta materia del sistema? Una vez realizado será irrevertible.", gridPane);
    		alert.getKey().addActions(
    				Map.entry(new MFXButton("Confirmar"), e -> {
    					SubjectController subjectController = SubjectController.getInstance();
    		    		subjectController.deleteSubject(selectedSubject);
    					subjects.remove(selectedSubject);
    					alert.getValue().close();
    				}),
    				Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close())
    		);
    		alert.getValue().showDialog();
    	}
    	else {
    		System.out.println("HOLA");
    		AlertManager.createError("Error", "Debe seleccionar a una materia antes de eliminarla", gridPane);
    	}
	}
}
