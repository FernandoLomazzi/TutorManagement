package controller.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;

import controller.AlertManager;
import controller.ValidatorManager;
import controller.model.TeacherController;
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

    public static ObservableList<Teacher> teachers;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
    	setupFields();
		setupTable();		
		teacherTable.autosizeColumnsOnInitialization();
		When.onChanged(teacherTable.tableRowFactoryProperty())
			.then((o, n) -> teacherTable.autosizeColumns()).listen();
		TeacherController teacherController = TeacherController.getInstance();
		teachers = FXCollections.observableArrayList(teacherController.getAllTeachers());
    	teacherTable.setItems(teachers);
	}
	private void setupFields() {
		ValidatorManager.notNullConstraint(nameField);
		ValidatorManager.notNullConstraint(surnameField);
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
	void generateReport(ActionEvent event) {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TeacherReportScreen.fxml"));
		try {
			Pane root = loader.load();
			((StackPane) gridPane.getParent()).getChildren().setAll(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @FXML
    void addTeacher(ActionEvent event) {
    	String name = nameField.getText().trim();
    	String surname = surnameField.getText().trim();
    	LocalDate birthday = birthdayField.getValue();
    	String description = descriptionField.getText().trim();
    	if(nameField.validate().isEmpty() && surnameField.validate().isEmpty()) {
    		TeacherController teacherController = TeacherController.getInstance();
    		Teacher newTeacher = teacherController.createTeacher(name, surname, birthday, description);
    		teachers.add(newTeacher);
        	nameField.clear();
        	nameField.deselect();
        	surnameField.clear();
        	surnameField.deselect();
        	birthdayField.setValue(null);
        	birthdayField.deselect();
        	descriptionField.clear();
        	descriptionField.deselect();
        	AlertManager.createInformation("Éxito", "El profesor se ha creado exitosamente", gridPane);
    	}
    	else {
    		AlertManager.createError("Error" , "Debe completar los campos nombre y apellido", gridPane);
    	}
    }

    @FXML
    void deleteTeacher(ActionEvent event) {
    	Teacher selectedTeacher = teacherTable.getSelectionModel().getSelectedValue();
    	if(selectedTeacher != null) {
    		Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado", "¿Desea eliminar este profesor del sistema? Una vez realizado será irrevertible.", gridPane);
    		alert.getKey().addActions(
    				Map.entry(new MFXButton("Confirmar"), e -> {
    		    		TeacherController teacherController = TeacherController.getInstance();
    		    		teacherController.deleteTeacher(selectedTeacher);
    					teachers.remove(selectedTeacher);
    					alert.getValue().close();
    				}),
    				Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close())
    		);
    		alert.getValue().showDialog();
    	}
    	else {
    		AlertManager.createError("Error", "Debe seleccionar a un profesor antes de eliminarlo", gridPane);
    	}
    }

}
