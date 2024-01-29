package com.tutormanagement.controller.view;

import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.tutormanagement.controller.model.LessonController;
import com.tutormanagement.controller.model.TeacherController;
import com.tutormanagement.model.TeacherReport;
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
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class TeacherReportScreenController implements Initializable {
	@FXML
	private GridPane gridPane;
	@FXML
	private MFXScrollPane scrollPane;
	@FXML
	private VBox vBox;

	private Map<String, List<TeacherReport>> teachers;

	// quizá poner un stack pane para cada maestro
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// scrollPane.setBorder(new Border(new BorderStroke(Color.BLACK,
		// BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		ScrollUtils.addSmoothScrolling(scrollPane);
		scrollPane.prefViewportHeightProperty().bind(gridPane.heightProperty().subtract(100));
		TeacherController teacherController = TeacherController.getInstance();
		try {
			teachers = teacherController.getUnpaidTeachers();
			teachers.forEach((teacher, teacherReport) -> {
				ObservableList<TeacherReport> teacherItems = FXCollections.observableArrayList(teacherReport);
				VBox vBoxInternal = new VBox(20);
				// vBoxInternal.setBorder(new Border(new BorderStroke(Color.BLACK,
				// BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				// add icon on hbox
				HBox hBox = new HBox(20);
				hBox.getChildren().add(new Label(teacher));
				MFXTableView<TeacherReport> table = new MFXTableView<>();
				table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				VBox.setVgrow(table, Priority.ALWAYS);
				table.setFooterVisible(false);
				table.autosizeColumnsOnInitialization();

				MFXTableColumn<TeacherReport> dayColumn = new MFXTableColumn<>("Día", true,
						Comparator.comparing(TeacherReport::getDay));

				MFXTableColumn<TeacherReport> studentsColumn = new MFXTableColumn<>("Estudiantes", true);
				MFXTableColumn<TeacherReport> subjectColumn = new MFXTableColumn<>("Materia", true,
						Comparator.comparing(TeacherReport::getSubjectName));
				MFXTableColumn<TeacherReport> institutionColumn = new MFXTableColumn<>("Institución", true,
						Comparator.comparing(TeacherReport::getInstName));
				MFXTableColumn<TeacherReport> totalHoursColumn = new MFXTableColumn<>("Horas", true,
						Comparator.comparing(TeacherReport::getTotalHours));
				Double total = teacherReport.stream().mapToDouble(TeacherReport::getTotal).sum();
				MFXTableColumn<TeacherReport> totalColumn = new MFXTableColumn<>("Total ($"+total+")", true,
						Comparator.comparing(TeacherReport::getTotal));

				MFXTableColumn<TeacherReport> isPaidColumn = new MFXTableColumn<>("Pagado", false);

				dayColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getDay));
				subjectColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getSubjectName));
				institutionColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getInstName));
				totalHoursColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getTotalHours));
				totalColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getTotal));
				studentsColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getStudentsName));
				/*
				 * studentsColumn.setRowCellFactory(device -> { MFXTableRowCell rowCell = new
				 * MFXTableRowCell<>(TeacherReport::getStudents) { //final MFXComboBox
				 * studentList = new MFXComboBox(); final MFXListView studentList = new
				 * MFXListView();
				 * 
				 * @Override public void update(TeacherReport item) {
				 * //studentList.setFloatMode(FloatMode.DISABLED);
				 * studentList.maxHeight(Double.MAX_VALUE); studentList.minHeight(200);
				 * studentList.prefHeight(200); studentList.setMaxSize(200, 200);
				 * studentList.setPrefSize(200, 200);
				 * 
				 * studentList.setItems(FXCollections.observableArrayList(item.getStudents()));
				 * setGraphic(studentList); };
				 * 
				 * }; rowCell.maxHeight(Double.MAX_VALUE); rowCell.minHeight(500);
				 * rowCell.prefHeight(500);
				 * 
				 * return rowCell; });
				 */
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
								alert.getKey().addActions(Map.entry(new MFXButton("Confirmar"), e -> {
									alert.getValue().close();
									TeacherReport teacher = table.getSelectionModel().getSelectedValue();
									try {
										LessonController lessonController = LessonController.getInstance();
										lessonController.makePayment(teacher);
										teacherItems.remove(teacher);
									} catch (SQLException ex) {
										AlertManager.createError("ERROR " + ex.getErrorCode(), ex.getMessage(),
												gridPane);
									}
								}), Map.entry(new MFXButton("Cancelar"), e -> {
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

				table.getTableColumns().addAll(dayColumn, studentsColumn, subjectColumn, institutionColumn,
						totalHoursColumn, totalColumn, isPaidColumn);

				table.setItems(teacherItems);
				table.prefHeightProperty().bind(Bindings.size(teacherItems).multiply(35).add(50));
				vBoxInternal.getChildren().addAll(hBox, table);
				StackPane pane = new StackPane();
				pane.getChildren().setAll(vBoxInternal);
				pane.getStyleClass().add("stack-pane");
				pane.setPadding(new Insets(20, 20, 20, 20));
				vBox.getChildren().add(pane);
			});
		} catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
		}
	}
}
