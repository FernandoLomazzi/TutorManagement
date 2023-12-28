package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.event.DocumentEvent.EventType;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import io.github.palexdev.materialfx.validation.Constraint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.EducationLevel;
import model.Student;

public class StudentScreenController implements Initializable{
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
    
    private ObservableList<Student> students;
	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	setupFields();
		setupTable();		
		studentTable.autosizeColumnsOnInitialization();
		When.onChanged(studentTable.tableRowFactoryProperty()).then((o, n) -> studentTable.autosizeColumns()).listen();
		
		/*When.onChanged(studentTable.currentPageProperty())
			.then((oldValue, newValue) -> studentTable.autosizeColumns())
			.listen();*/
		students = FXCollections.observableArrayList();
    	Student st1 = new Student("Pepe", "Pepito", "Agustin delgado 1952", "342-5157224", LocalDate.now(), "@Juancito", "No se es un pibe re loco jajajja sjadalkdjlad alkdjaldasjd laks", EducationLevel.INGRESO);
    	Student st2 = new Student("Juan", "Juancito", "Padilla 5142", "3425155432", LocalDate.now(), "@Ferchomax", "", EducationLevel.INGRESO);
    	Student st3 = new Student("Roberto", "Robertito", "General Paz 1209", "343 3212413", LocalDate.now(), "Jiji", "", EducationLevel.INGRESO);
    	students.addAll(st1, st2, st3);
		studentTable.setItems(students);	
	}
	private void setupFields() {
		nameField.getValidator().constraint("Test", nameField.textProperty().length().greaterThan(0));
		nameField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				System.out.println("SI valida");
				nameField.pseudoClassStateChanged(PseudoClass.getPseudoClass("invalid"), false);
			}
		});
		nameField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
			if(oldValue && !newValue) {
				List<Constraint> constraints = nameField.validate();
				if(!constraints.isEmpty()) {
					System.out.println("NO valida");
					nameField.pseudoClassStateChanged(PseudoClass.getPseudoClass("invalid"), true);
					
					//validationLabel
				}
			}
		});
		edLevelField.setItems(FXCollections.observableArrayList(EducationLevel.values()));
		/*passwordField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue && !newValue) {
				List<Constraint> constraints = passwordField.validate();
				if (!constraints.isEmpty()) {
					passwordField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
					validationLabel.setText(constraints.get(0).getMessage());
					validationLabel.setVisible(true);
				}
			}
		});*/
		//studentTable.setRowsPerPage(14);
	}
	private void setupTable() {
		MFXTableColumn<Student> nameColumn = new MFXTableColumn<>("Nombre", false, Comparator.comparing(Student::getName));
		MFXTableColumn<Student> surnameColumn = new MFXTableColumn<>("Apellido", false, Comparator.comparing(Student::getSurname));
		MFXTableColumn<Student> addressColumn = new MFXTableColumn<>("Dirección", false, Comparator.comparing(Student::getAddress));
		MFXTableColumn<Student> phoneNumberColumn = new MFXTableColumn<>("Número", false, Comparator.comparing(Student::getPhoneNumber));
		MFXTableColumn<Student> birthdayColumn = new MFXTableColumn<>("Cumpleaños", false, Comparator.comparing(Student::getBirthday));
		MFXTableColumn<Student> socialMediaColumn = new MFXTableColumn<>("Red social", false, Comparator.comparing(Student::getSocialMedia));
		MFXTableColumn<Student> levelColumn = new MFXTableColumn<>("Nivel", false, Comparator.comparing(Student::getEducationLevel));
		
		//MFXTableColumn<Student> descriptionColumn = new MFXTableColumn<>("Descripción", false, Comparator.comparing(Student::getDescription));
		nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getName));
		surnameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getSurname));
		addressColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getAddress));
		phoneNumberColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getPhoneNumber));
		birthdayColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getBirthday));
		socialMediaColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getSocialMedia));
		levelColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getEducationLevelInitial));
		
		//descriptionColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getDescription));
		
		studentTable.getTableColumns().addAll(nameColumn, surnameColumn, addressColumn, phoneNumberColumn, birthdayColumn, socialMediaColumn, levelColumn);

		studentTable.getFilters().addAll(
				new StringFilter<>("Nombre", Student::getName),
				new StringFilter<>("Apellido", Student::getSurname)
		);
	}
	
    @FXML
    void addStudent(ActionEvent event) {
    	String name = nameField.getText().trim();
    	String surname = surnameField.getText().trim();
    	String address = addressField.getText().trim();
    	String phoneNumber = phoneNumberField.getText().trim();
    	LocalDate birthday = birthdayField.getValue();
    	String socialMedia = socialMediaField.getText().trim();
    	String description = descriptionField.getText().trim();
    	EducationLevel lvl = edLevelField.getSelectedItem();
    	System.out.println("---");
    	System.out.println(name);
    	System.out.println(surname);
    	System.out.println(address);
    	System.out.println(birthday);
    	System.out.println(socialMedia);
    	System.out.println(description);
    	if(nameField.validate().isEmpty() && surnameField.validate().isEmpty()) {
    		Student newStudent = new Student(name, surname, address, phoneNumber, birthday, socialMedia, description, lvl);
    		// agregar gestor.
    		students.add(newStudent);
    		// Ventana de se realizó la operacion con exito.
        	nameField.clear();
        	surnameField.clear();
        	addressField.clear();
        	phoneNumberField.clear();
        	birthdayField.clear();
        	birthdayField.setValue(null);
        	socialMediaField.clear();
        	descriptionField.clear();
        	edLevelField.clearSelection();
    	}
    	else {
    		// mensaje de error capaz
    		System.out.println("Error");
    	}
    }

    @FXML
    void deleteStudent(ActionEvent event) {
    	Student selectedStudent = studentTable.getSelectionModel().getSelectedValue();
    	if(selectedStudent != null) {
    		Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado", "¿Desea eliminar este estudiante del sistema? Una vez realizado será irrevertible.", gridPane);
    		alert.getKey().addActions(
    				Map.entry(new MFXButton("Confirmar"), e -> {
    					students.remove(studentTable.getSelectionModel().getSelectedValue());
    					alert.getValue().close();
    				}),
    				Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close())
    		);
    		alert.getValue().showDialog();
    	}
    	else {
    		System.out.println("HOLA");
    		AlertManager.createError("Error", "Debe seleccionar a un estudiante antes de eliminarlo", gridPane);
    		
    		//MFXGenericDialogBuilder.toStageDialogBuilder().get().build(error);
    		//AlertManager.createError("Error", "Debe seleccionar un alumno para borrar");
    	}
    }

}