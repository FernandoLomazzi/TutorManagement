package controller.view;


import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import controller.model.LessonController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.EnumFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.BorderPane;
import model.Commission;
import model.EducationLevel;
import model.Lesson;
import model.Payment;
import model.Student;
import model.Subject;
import model.Teacher;
import utils.AlertManager;
import utils.ValidatorManager;

public class LessonScreenController implements Initializable{
    @FXML
    private BorderPane borderPane;
    @FXML
    private MFXButton createButton;
    @FXML
    private MFXDatePicker dateField;
    @FXML
    private MFXTextField pricePerHourField;
    @FXML
    private MFXTextField pricePerHourTeacherField;
    @FXML
    private MFXTableView<Student> studentTable;
    @FXML
    private MFXFilterComboBox<Subject> subjectField;
    @FXML
    private MFXFilterComboBox<Teacher> teacherField;
    @FXML
    private MFXRectangleToggleNode teacherPaidToggle;
    @FXML
    private MFXTextField totalHoursField;
    
    private ObservableList<Student> students;
    private ObservableList<Subject> subjects;
    private ObservableList<Teacher> teachers;

    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("CARGANDO");
    	setupFields();
		setupTable();		
		studentTable.autosizeColumnsOnInitialization();
		When.onChanged(studentTable.tableRowFactoryProperty())
			.then((o, n) -> studentTable.autosizeColumns()).listen();
		students = StudentScreenController.students;
		studentTable.setItems(students);
		subjects = SubjectScreenController.subjects;
		subjectField.setItems(subjects);
		teachers = TeacherScreenController.teachers;
		teacherField.setItems(teachers);
	}
	private void setupFields() {
		ValidatorManager.notNullConstraint(dateField);
		ValidatorManager.notNullConstraint(subjectField);
		ValidatorManager.notNullConstraint(pricePerHourField);
		ValidatorManager.notNullConstraint(totalHoursField);	
	}
	private void setupTable() {
		MFXTableColumn<Student> nameColumn = new MFXTableColumn<>("Nombre", true, Comparator.comparing(Student::getName));
		MFXTableColumn<Student> surnameColumn = new MFXTableColumn<>("Apellido", true, Comparator.comparing(Student::getSurname));
		MFXTableColumn<Student> levelColumn = new MFXTableColumn<>("Nivel", true, Comparator.comparing(Student::getEducationLevel));
		MFXTableColumn<Student> selectionColumn = new MFXTableColumn<>("Seleccionar", false);
		MFXTableColumn<Student> paidColumn = new MFXTableColumn<>("Pagado", false);
		
		nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getName));
		surnameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getSurname));
		levelColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getEducationLevelInitial));
		
		selectionColumn.setRowCellFactory(device -> { 
			MFXTableRowCell rc = new MFXTableRowCell<>(null, s->"");
			MFXCheckbox selectedCheck = new MFXCheckbox();
			selectedCheck.setContentDisposition(ContentDisplay.GRAPHIC_ONLY);
			rc.setLeadingGraphic(selectedCheck);
			return rc;
		});
		paidColumn.setRowCellFactory(device -> {
			MFXTableRowCell rc = new MFXTableRowCell<>(null, s -> "");
			MFXCheckbox paidCheck = new MFXCheckbox();
			paidCheck.setContentDisposition(ContentDisplay.GRAPHIC_ONLY);
			rc.setLeadingGraphic(paidCheck);
			return rc;
		});
		
		studentTable.getTableColumns().addAll(nameColumn, surnameColumn, levelColumn, selectionColumn, paidColumn);

		studentTable.getFilters().addAll(
				new StringFilter<>("Nombre", Student::getName),
				new StringFilter<>("Apellido", Student::getSurname),
				new EnumFilter<>("Nivel", Student::getEducationLevel, EducationLevel.class)
		);
	}

    @FXML
    void createLesson(ActionEvent event) {
    	if(!(subjectField.validate().isEmpty() && dateField.validate().isEmpty() && 
    			pricePerHourField.validate().isEmpty() && totalHoursField.validate().isEmpty())) {
			//error campos vacios
    		System.out.println("ERROR");
    		return;
    	}
    	Subject subject = subjectField.getValue();
    	LocalDate day = dateField.getValue();
    	Boolean paidTeacher = teacherPaidToggle.isSelected();
    	Teacher teacher = teacherField.getSelectedItem();
    	Integer totalHours;
    	Double pricePerHour;
    	Double pricePerHourTeacher;
    	try {
        	totalHours = Integer.parseInt(totalHoursField.getText().trim());
        	pricePerHour = Double.parseDouble(pricePerHourField.getText().trim());
        	pricePerHourTeacher = Double.parseDouble(pricePerHourTeacherField.getText().trim());
    	}catch(Exception e) {
    		e.getMessage();
    		return;
    	}
    	LessonController lessonController = LessonController.getInstance();
    	Lesson lesson = new Lesson();
    	List<Payment> payments =  new ArrayList<>();
    	studentTable.getCells().forEach((number, row)->{
    		MFXCheckbox selR = (MFXCheckbox) row.getCells().get(3).getLeadingGraphic();
    		MFXCheckbox paidR = (MFXCheckbox) row.getCells().get(4).getLeadingGraphic();
    		if(selR.isSelected()) {
    			Student student = studentTable.getCell(number).getData();
    			Payment payment = new Payment(student, lesson, paidR.isSelected(), paidR.isSelected());
    			payments.add(payment);
    		}
    	});
    	if(payments.isEmpty()) {
    		//error
    		System.out.println("ERROR");
    		return;
    	}
    	Double totalTeacher = totalHours*pricePerHourTeacher;
    	Commission comm = new Commission(lesson, teacher, pricePerHourTeacher, totalTeacher, paidTeacher);
    	lesson.setDay(day);
    	lesson.setPayment(payments);
    	lesson.setPricePerHour(pricePerHour);
    	lesson.setSubject(subject);
    	lesson.setTotalHours(totalHours);
    	lesson.setCommission(comm);
    	lesson.updateState();
    	lessonController.createLesson(lesson);
    	AlertManager.createInformation("Éxito", "La clase se ha creado exitosamente", borderPane);
    	subjectField.clear();
    	pricePerHourField.clear();
    	totalHoursField.clear();
    	dateField.clear();
    	teacherField.clear();
    	pricePerHourTeacherField.clear();
    	teacherPaidToggle.setSelected(false);
    	studentTable.getCells().forEach((number, row)->{
    		MFXCheckbox selR = (MFXCheckbox) row.getCells().get(3).getLeadingGraphic();
    		MFXCheckbox paidR = (MFXCheckbox) row.getCells().get(4).getLeadingGraphic();
    		selR.setSelected(false);
    		paidR.setSelected(false);
    	});
    }
}
