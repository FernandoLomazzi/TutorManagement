package com.tutormanagement.controller.view;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.tutormanagement.controller.model.LessonController;
import com.tutormanagement.controller.model.StudentController;
import com.tutormanagement.controller.model.SubjectController;
import com.tutormanagement.controller.model.TeacherController;
import com.tutormanagement.model.Commission;
import com.tutormanagement.model.EducationLevel;
import com.tutormanagement.model.Lesson;
import com.tutormanagement.model.Payment;
import com.tutormanagement.model.Student;
import com.tutormanagement.model.Subject;
import com.tutormanagement.model.Teacher;
import com.tutormanagement.utils.AlertManager;
import com.tutormanagement.utils.MaterialFXManager;
import com.tutormanagement.utils.ValidatorManager;

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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class LessonScreenController implements Initializable {
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
	private MFXTableView<StudentView> studentTable;
	@FXML
	private MFXFilterComboBox<Subject> subjectField;
	@FXML
	private MFXFilterComboBox<Teacher> teacherField;
	@FXML
	private MFXRectangleToggleNode teacherPaidToggle;
	@FXML
	private MFXTextField totalHoursField;

	private ObservableList<StudentView> students;
	private ObservableList<Subject> subjects;
	private ObservableList<Teacher> teachers;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupFields();
		setupTable();
		studentTable.autosizeColumnsOnInitialization();
		// Check this
		When.onChanged(studentTable.tableRowFactoryProperty()).then((o, n) -> studentTable.autosizeColumns()).listen();
		try {
			StudentController studentController = StudentController.getInstance();
			students = FXCollections.observableArrayList(studentController.getAllStudents().stream()
					.map(student -> new StudentView(student)).collect(Collectors.toList()));
			studentTable.setItems(students);

			SubjectController subjectController = SubjectController.getInstance();
			subjects = FXCollections.observableArrayList(subjectController.getAllSubjects());
			subjectField.setItems(subjects);

			TeacherController teacherController = TeacherController.getInstance();
			teachers = FXCollections.observableArrayList(teacherController.getAllTeachers());
			teacherField.setItems(teachers);
		} catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), borderPane);
		}
	}

	private void setupFields() {
		ValidatorManager.notNullConstraint(subjectField);
		ValidatorManager.notNullConstraint(pricePerHourField);
		ValidatorManager.notNullConstraint(totalHoursField);
		ValidatorManager.notNullConstraint(dateField);
		ValidatorManager.notNullConstraint(teacherField);
		ValidatorManager.notNullConstraint(pricePerHourTeacherField);
		// add only numbers in some fields
		teacherField.setOnAction(event -> {
			Teacher selTeacher = teacherField.getSelectedItem();
			if(selTeacher!=null && selTeacher.getName().equals("Ingres") && selTeacher.getSurname().equals("Ar")) {
				pricePerHourTeacherField.setDisable(true);
				pricePerHourTeacherField.setText("0");
				teacherPaidToggle.setDisable(true);
				teacherPaidToggle.setSelected(true);				
			}
			else if(pricePerHourTeacherField.isDisable() && teacherPaidToggle.isDisable()){
				MaterialFXManager.clearAllFields(pricePerHourTeacherField);
				pricePerHourTeacherField.setDisable(false);
				teacherPaidToggle.setSelected(false);
				teacherPaidToggle.setDisable(false);
			}
		});
	}

	private void setupTable() {
		MFXTableColumn<StudentView> nameColumn = new MFXTableColumn<>("Nombre", true,
				Comparator.comparing(Student::getName));
		MFXTableColumn<StudentView> surnameColumn = new MFXTableColumn<>("Apellido", true,
				Comparator.comparing(Student::getSurname));
		MFXTableColumn<StudentView> levelColumn = new MFXTableColumn<>("Nivel", true,
				Comparator.comparing(Student::getEducationLevel));
		MFXTableColumn<StudentView> selectionColumn = new MFXTableColumn<>("Seleccionar", false,
				Comparator.comparing(StudentView::isSelected));
		MFXTableColumn<StudentView> paidColumn = new MFXTableColumn<>("Pagado", false,
				Comparator.comparing(StudentView::isPaid));

		nameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getName));
		surnameColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getSurname));
		levelColumn.setRowCellFactory(device -> new MFXTableRowCell<>(Student::getEducationLevelInitial));

		selectionColumn.setRowCellFactory(device -> new MFXTableRowCell<>(s -> "") {
			final MFXCheckbox selectedCheck = new MFXCheckbox();
			{
				setGraphic(selectedCheck);
			}

			@Override
			public void update(StudentView student) {
				selectedCheck.setSelected(student.isSelected());
				selectedCheck.setOnAction(event -> {
					student.setSelected(selectedCheck.isSelected());
				});
			}
		});
		paidColumn.setRowCellFactory(device -> new MFXTableRowCell<>(s -> "") {
			final MFXCheckbox selectedCheck = new MFXCheckbox();
			{
				setGraphic(selectedCheck);
			}

			@Override
			public void update(StudentView student) {
				selectedCheck.setSelected(student.isPaid());
				selectedCheck.setOnAction(event -> {
					student.setPaid(selectedCheck.isSelected());
				});
			}
		});

		studentTable.getTableColumns().addAll(nameColumn, surnameColumn, levelColumn, selectionColumn, paidColumn);

		studentTable.getFilters().addAll(new StringFilter<>("Nombre", Student::getName),
				new StringFilter<>("Apellido", Student::getSurname),
				new EnumFilter<>("Nivel", Student::getEducationLevel, EducationLevel.class));
		studentTable.setFooterVisible(true);
	}

	private Boolean checkFieldConstraints() {
		return subjectField.isValid() && pricePerHourField.isValid() && totalHoursField.isValid() && dateField.isValid()
				&& teacherField.isValid() && pricePerHourTeacherField.isValid();
	}

	@FXML
	void createLesson(ActionEvent event) {
		if (!checkFieldConstraints()) {
			AlertManager.createError("Error", "Complete todos los campos obligatorios antes de agregar una clase",
					borderPane);
			return;
		}
		Subject subject = subjectField.getValue();
		LocalDate day = dateField.getValue();
		Boolean paidTeacher = teacherPaidToggle.isSelected();
		Teacher teacher = teacherField.getSelectedItem();
		Double totalHours;
		Double pricePerHour;
		Double pricePerHourTeacher;
		try {
			totalHours = Double.parseDouble(totalHoursField.getText().trim());
			pricePerHour = Double.parseDouble(pricePerHourField.getText().trim());
			pricePerHourTeacher = Double.parseDouble(pricePerHourTeacherField.getText().trim());
		} catch (Exception e) {
			AlertManager.createError("Error", "Complete los campos numéricos con números válidos", borderPane);
			return;
		}
		try {
			LessonController lessonController = LessonController.getInstance();
			Lesson lesson = new Lesson();
			List<Payment> payments = new ArrayList<>();
			studentTable.getCells().forEach((number, row) -> {
				StudentView s = row.getData();
				if (s.isSelected()) {
					Student student = new Student(s.getName(), s.getSurname(), s.getAddress(), s.getPhoneNumber(),
							s.getBirthday(), s.getSocialMedia(), s.getDescription(), s.getEducationLevel());
					Payment payment = new Payment(student, lesson, s.isPaid(), s.isPaid());
					payments.add(payment);
					System.out.println(student.getName() + " " + payment.isPaid());
				}
			});
			if (payments.isEmpty()) {
				AlertManager.createError("Error", "Seleccione al menos un alumno para crear una clase", borderPane);
				return;
			}
			Double totalTeacher = totalHours * pricePerHourTeacher;
			Commission comm = new Commission(lesson, teacher, pricePerHourTeacher, totalTeacher, paidTeacher);
			lesson.setDay(day);
			lesson.setPayment(payments);
			lesson.setPricePerHour(pricePerHour);
			lesson.setSubject(subject);
			lesson.setTotalHours(totalHours);
			lesson.setCommission(comm);
			lessonController.createLesson(lesson);

			AlertManager.createInformation("Éxito", "La clase se ha creado exitosamente", borderPane);
			MaterialFXManager.clearAllFields(subjectField, pricePerHourField, totalHoursField, dateField, teacherField,
					pricePerHourTeacherField);
			teacherPaidToggle.setSelected(false);
			studentTable.getCells().forEach((number, row) -> {
				((MFXCheckbox) row.getCells().get(3).getGraphic()).setSelected(false);
				((MFXCheckbox) row.getCells().get(4).getGraphic()).setSelected(false);
			});
		} catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), borderPane);
		}

	}

	private class StudentView extends Student {
		private Boolean isSelected;
		private Boolean isPaid;

		public StudentView(Student student) {
			super(student.getName(), student.getSurname(), student.getAddress(), student.getPhoneNumber(),
					student.getBirthday(), student.getSocialMedia(), student.getDescription(),
					student.getEducationLevel());
			isSelected = false;
			isPaid = false;
		}

		public Boolean isSelected() {
			return isSelected;
		}

		public Boolean isPaid() {
			return isPaid;
		}

		public void setSelected(Boolean selected) {
			isSelected = selected;
		}

		public void setPaid(Boolean paid) {
			isPaid = paid;
		}
	}
}
