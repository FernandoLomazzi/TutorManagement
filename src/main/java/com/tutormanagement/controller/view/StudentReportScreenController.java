package com.tutormanagement.controller.view;

import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.tutormanagement.controller.model.LessonController;
import com.tutormanagement.controller.model.StudentController;
import com.tutormanagement.model.StudentReport;
import com.tutormanagement.utils.AlertManager;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.mfxcore.utils.fx.ScrollUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class StudentReportScreenController implements Initializable {
	@FXML
	private GridPane gridPane;
	@FXML
	private MFXScrollPane scrollPane;
	@FXML
	private VBox vBox;

	private Map<String, List<StudentReport>> students;

	// quizá poner un stack pane para cada alumno
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// scrollPane.setBorder(new Border(new BorderStroke(Color.BLACK,
		// BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		ScrollUtils.addSmoothScrolling(scrollPane);
		scrollPane.prefViewportHeightProperty().bind(gridPane.heightProperty().subtract(100));
		StudentController studentController = StudentController.getInstance();
		try {
			students = studentController.getUnpaidStudents();
			students.forEach((student, studentReport) -> {
				ObservableList<StudentReport> studentItems = FXCollections.observableArrayList(studentReport);
				VBox vBoxInternal = new VBox(20);
				// vBoxInternal.setBorder(new Border(new BorderStroke(Color.BLACK,
				// BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				// add icon on hbox
				HBox hBox = new HBox(20);
				hBox.getChildren().add(new Label(student));
				MFXTableView<StudentReport> table = new MFXTableView<>();
				table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				VBox.setVgrow(table, Priority.ALWAYS);
				table.setFooterVisible(false);
				table.autosizeColumnsOnInitialization();
				MFXTableColumn<StudentReport> totalHoursColumn = new MFXTableColumn<>("Horas", true,
						Comparator.comparing(StudentReport::getTotalHours));
				MFXTableColumn<StudentReport> dayColumn = new MFXTableColumn<>("Día", true,
						Comparator.comparing(StudentReport::getDay));
				MFXTableColumn<StudentReport> teacherColumn = new MFXTableColumn<>("Profesor", true,
						Comparator.comparing(StudentReport::getTeacherCompleteName));
				MFXTableColumn<StudentReport> subjectColumn = new MFXTableColumn<>("Materia", true,
						Comparator.comparing(StudentReport::getSubjectName));
				MFXTableColumn<StudentReport> institutionColumn = new MFXTableColumn<>("Institución", true,
						Comparator.comparing(StudentReport::getInstitutionName));
				MFXTableColumn<StudentReport> totalColumn = new MFXTableColumn<>("Total ($)", true,
						Comparator.comparing(StudentReport::getTotal));

				MFXTableColumn<StudentReport> notifiedColumn = new MFXTableColumn<>("Notificado", false,
						Comparator.comparing(StudentReport::isNotified));
				MFXTableColumn<StudentReport> isPaidColumn = new MFXTableColumn<>("Pagado", false);

				totalHoursColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getTotalHours));
				dayColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getDay));
				teacherColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getTeacherCompleteName));
				subjectColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getSubjectName));
				institutionColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getInstitutionName));
				totalColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getTotal));

				notifiedColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::isNotified) {
					final MFXCheckbox selectedCheck = new MFXCheckbox();

					private void handleCheckbox(StudentReport item) {
						selectedCheck.setSelected(item.isNotified());
						selectedCheck.setOnAction(event -> {
							System.out.println("ACTION");
							StudentReport student = table.getSelectionModel().getSelectedValue();
							try {
								LessonController lessonController = LessonController.getInstance();
								lessonController.setNotified(student, selectedCheck.isSelected());
								item.setNotified(selectedCheck.isSelected());
							} catch (SQLException e) {
								AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
							}
						});
						selectedCheck.setContentDisposition(ContentDisplay.GRAPHIC_ONLY);
					}

					@Override
					public void update(StudentReport item) {
						handleCheckbox(item);
						setGraphic(selectedCheck);
					}
				});
				isPaidColumn.setRowCellFactory(device -> {
					MFXTableRowCell rc = new MFXTableRowCell<>(s -> "");
					MFXCheckbox selectedCheck = new MFXCheckbox();

					selectedCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
								Boolean newValue) {
							if (newValue) {
								Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado",
										"¿Desea marcar esta clase como pagada?.", gridPane);
								alert.getKey().addActions(Map.entry(new MFXButton("Confirmar"), event -> {
									alert.getValue().close();
									StudentReport student = table.getSelectionModel().getSelectedValue();
									try {
										LessonController lessonController = LessonController.getInstance();
										lessonController.makePayment(student);
										studentItems.remove(student);
									} catch (SQLException e) {
										AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
									}
								}), Map.entry(new MFXButton("Cancelar"), event -> {
									alert.getValue().close();
									selectedCheck.setSelected(false);
								}));
								alert.getValue().showDialog();
							}
						}
					});
					selectedCheck.setContentDisposition(ContentDisplay.GRAPHIC_ONLY);
					rc.setGraphic(selectedCheck);
					return rc;
				});

				table.getTableColumns().addAll(dayColumn, teacherColumn, subjectColumn, institutionColumn,
						totalHoursColumn, totalColumn, notifiedColumn, isPaidColumn);

				table.setItems(studentItems);
				table.prefHeightProperty().bind(Bindings.size(studentItems).multiply(35).add(50));
				vBoxInternal.getChildren().addAll(hBox, table);
				vBox.getChildren().add(vBoxInternal);
			});
		} catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
		}
	}
}
