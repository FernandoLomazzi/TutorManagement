package controller.view;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;

import controller.AlertManager;
import controller.ValidatorManager;
import controller.model.StudentController;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
    @FXML
    private MFXButton reportButton;
    
    public static ObservableList<Student> students;
	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	setupFields();
		setupTable();		
		studentTable.autosizeColumnsOnInitialization();
		When.onChanged(studentTable.tableRowFactoryProperty())
			.then((o, n) -> studentTable.autosizeColumns()).listen();
		StudentController studentController = StudentController.getInstance();
		students = FXCollections.observableArrayList(studentController.getAllStudents());    	
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
				new StringFilter<>("Apellido", Student::getSurname),
				new EnumFilter<>("Nivel", Student::getEducationLevel, EducationLevel.class)
		);
	}
	
    @FXML
    void generateReport(ActionEvent event) {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudentReportScreen.fxml"));
		try {
			Pane root = loader.load();
			((StackPane) gridPane.getParent()).getChildren().setAll(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
    	
    	if(nameField.validate().isEmpty() && surnameField.validate().isEmpty() && lvl!=null) {
    		StudentController studentController = StudentController.getInstance();
    		//try - catch
    		Student newStudent = studentController.createStudent(name, surname, address, phoneNumber, birthday, socialMedia, description, lvl);    		
    		students.add(newStudent);
    		
    		nameField.clear();
    		nameField.deselect();
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
    					StudentController studentController = StudentController.getInstance();
    					studentController.deleteStudent(selectedStudent);
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