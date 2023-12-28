package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
		When.onChanged(studentTable.tableRowFactoryProperty())
			.then((o, n) -> studentTable.autosizeColumns()).listen();
		students = FXCollections.observableArrayList();
    	Student st1 = new Student("Pepe", "Pepito", "Agustin delgado 1952", "342-5157224", LocalDate.now(), "@Juancito", "No se es un pibe re loco jajajja sjadalkdjlad alkdjaldasjd laks", EducationLevel.INGRESO);
    	Student st2 = new Student("Juan", "Juancito", "Padilla 5142", "3425155432", LocalDate.now(), "@Ferchomax", "", EducationLevel.INGRESO);
    	Student st3 = new Student("Roberto", "Robertito", "General Paz 1209", "343 3212413", LocalDate.now(), "Jiji", "", EducationLevel.INGRESO);
    	students.addAll(st1, st2, st3);
		studentTable.setItems(students);
	}
	private void setupFields() {
		edLevelField.setItems(FXCollections.observableArrayList(EducationLevel.values()));
		edLevelField.selectFirst();
		ValidatorManager.notNullConstraint(nameField);
		ValidatorManager.notNullConstraint(surnameField);
	}
	private void setupTable() {
		MFXTableColumn<Student> nameColumn = new MFXTableColumn<>("Nombre", false, Comparator.comparing(Student::getName));
		MFXTableColumn<Student> surnameColumn = new MFXTableColumn<>("Apellido", false, Comparator.comparing(Student::getSurname));
		MFXTableColumn<Student> addressColumn = new MFXTableColumn<>("Dirección", false, Comparator.comparing(Student::getAddress));
		MFXTableColumn<Student> phoneNumberColumn = new MFXTableColumn<>("Número", false, Comparator.comparing(Student::getPhoneNumber));
		MFXTableColumn<Student> birthdayColumn = new MFXTableColumn<>("Cumpleaños", false, Comparator.comparing(Student::getBirthday));
		MFXTableColumn<Student> socialMediaColumn = new MFXTableColumn<>("Red social", false, Comparator.comparing(Student::getSocialMedia));
		MFXTableColumn<Student> levelColumn = new MFXTableColumn<>("Nivel", false, Comparator.comparing(Student::getEducationLevel));
		
		nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getName));
		surnameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getSurname));
		addressColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getAddress));
		phoneNumberColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getPhoneNumber));
		birthdayColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getBirthday));
		socialMediaColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getSocialMedia));
		levelColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getEducationLevelInitial));
		
		studentTable.getTableColumns().addAll(nameColumn, surnameColumn, addressColumn, phoneNumberColumn, birthdayColumn, socialMediaColumn, levelColumn);

		studentTable.getFilters().addAll(
				new StringFilter<>("Nombre", Student::getName),
				new StringFilter<>("Apellido", Student::getSurname)
		);
	}
	
    @FXML
    void addStudent(ActionEvent event) {
		System.out.println(studentTable.getSelectionModel().getSelectedValues());
    	String name = nameField.getText().trim();
    	String surname = surnameField.getText().trim();
    	String address = addressField.getText().trim();
    	String phoneNumber = phoneNumberField.getText().trim();
    	LocalDate birthday = birthdayField.getValue();
    	String socialMedia = socialMediaField.getText().trim();
    	String description = descriptionField.getText().trim();
    	EducationLevel lvl = edLevelField.getSelectedItem();
    	
    	if(nameField.validate().isEmpty() && surnameField.validate().isEmpty()) {
    		Student newStudent = new Student(name, surname, address, phoneNumber, birthday, socialMedia, description, lvl);
    		// $ agregar gestor.
    		
    		students.add(newStudent);
    		// Ventana de se realizó la operacion con exito.
    		nameField.end();
    		nameField.clear();
    		//nameField.clear();
        	surnameField.clear();
        	surnameField.deselect();
        	addressField.clear();
        	addressField.deselect();
        	phoneNumberField.clear();
        	phoneNumberField.deselect();
        	birthdayField.setValue(null);
        	birthdayField.deselect();
        	socialMediaField.clear();
        	socialMediaField.deselect();
        	descriptionField.clear();
        	descriptionField.deselect();
        	//edLevelField.selectFirst();
        	AlertManager.createInformation("Éxito", "El estudiante se ha creado exitosamente", gridPane);
    	}
    	else {
    		AlertManager.createError("Error" , "Debe completar los campos nombre y apellido", gridPane);
    	}
    }

    @FXML
    void deleteStudent(ActionEvent event) {
    	Student selectedStudent = studentTable.getSelectionModel().getSelectedValue();
    	if(selectedStudent != null) {
    		Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado", "¿Desea eliminar este estudiante del sistema? Una vez realizado será irrevertible.", gridPane);
    		alert.getKey().addActions(
    				Map.entry(new MFXButton("Confirmar"), e -> {
    					// $ agregar gestor
    					students.remove(selectedStudent);
    					alert.getValue().close();
    				}),
    				Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close())
    		);
    		alert.getValue().showDialog();
    	}
    	else {
    		AlertManager.createError("Error", "Debe seleccionar a un estudiante antes de eliminarlo", gridPane);
    	}
    }

}