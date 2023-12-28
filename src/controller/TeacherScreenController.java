package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.EducationLevel;
import model.Student;
import model.Teacher;

public class TeacherScreenController implements Initializable{
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
		
		teachers = FXCollections.observableArrayList();
		Teacher tch1 = new Teacher("Pepe", "Pepito", LocalDate.now(), "Hola que tal");
		Teacher tch2 = new Teacher("Juan", "Juancito", LocalDate.now(), "Douas");
		Teacher tch3 = new Teacher("Pedro", "Perez", LocalDate.now(), "");
    	teachers.addAll(tch1, tch2, tch3);
		teacherTable.setItems(teachers);	
	}
	private void setupFields() {
		// to-do
	}
	private void setupTable() {
		MFXTableColumn<Teacher> nameColumn = new MFXTableColumn<>("Nombre", false, Comparator.comparing(Teacher::getName));
		MFXTableColumn<Teacher> surnameColumn = new MFXTableColumn<>("Apellido", false, Comparator.comparing(Teacher::getSurname));
		MFXTableColumn<Teacher> birthdayColumn = new MFXTableColumn<>("Cumpleaños", false, Comparator.comparing(Teacher::getBirthday));
		
		nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Teacher::getName));
		surnameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Teacher::getSurname));
		birthdayColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Teacher::getBirthday));
		
		teacherTable.getTableColumns().addAll(nameColumn, surnameColumn, birthdayColumn);

		teacherTable.getFilters().addAll(
				new StringFilter<>("Nombre", Teacher::getName),
				new StringFilter<>("Apellido", Teacher::getSurname)
		);
	}
	
    @FXML
    void addTeacher(ActionEvent event) {
    	String name = nameField.getText().trim();
    	String surname = surnameField.getText().trim();
    	LocalDate birthday = birthdayField.getValue();
    	String description = descriptionField.getText().trim();
    	if(nameField.validate().isEmpty() && surnameField.validate().isEmpty()) {
    		Teacher newTeacher = new Teacher(name, surname, birthday, description);
    		// agregar gestor.
    		teachers.add(newTeacher);
    		// Ventana de se realizó la operacion con exito.
        	nameField.clear();
        	surnameField.clear();
        	birthdayField.clear();
        	birthdayField.setValue(null);
        	descriptionField.clear();
    	}
    	else {
    		// mensaje de error capaz
    		System.out.println("Error");
    	}
    }

    @FXML
    void deleteTeacher(ActionEvent event) {
    	Teacher selectedTeacher = teacherTable.getSelectionModel().getSelectedValue();
    	if(selectedTeacher != null) {
    		Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado", "¿Desea eliminar este profesor del sistema? Una vez realizado será irrevertible.", gridPane);
    		alert.getKey().addActions(
    				Map.entry(new MFXButton("Confirmar"), e -> {
    					teachers.remove(teacherTable.getSelectionModel().getSelectedValue());
    					alert.getValue().close();
    				}),
    				Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close())
    		);
    		alert.getValue().showDialog();
    	}
    	else {
    		System.out.println("HOLA");
    		AlertManager.createError("Error", "Debe seleccionar a un profesor antes de eliminarlo", gridPane);
    		
    		//MFXGenericDialogBuilder.toStageDialogBuilder().get().build(error);
    		//AlertManager.createError("Error", "Debe seleccionar un alumno para borrar");
    	}
    }

}
