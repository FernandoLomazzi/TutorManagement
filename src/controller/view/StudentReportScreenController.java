package controller.view;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import controller.model.LessonController;
import controller.model.StudentController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import model.StudentReport;
import utils.AlertManager;

public class StudentReportScreenController implements Initializable{
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
		scrollPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		scrollPane.prefViewportHeightProperty().bind(gridPane.heightProperty().subtract(100));
		StudentController studentController = StudentController.getInstance();
		students = studentController.getUnpaidStudents();
		students.forEach((student, studentReport) -> {
			ObservableList<StudentReport> studentItems = FXCollections.observableArrayList(studentReport);
			VBox vBoxInternal = new VBox(20);
			vBoxInternal.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			//add icon on hbox
			HBox hBox = new HBox(20);
			System.out.println(hBox.getPrefHeight() + " " + hBox.getPrefWidth());
			hBox.getChildren().add(new Label(student));
			MFXTableView<StudentReport> table = new  MFXTableView<>();
			table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			VBox.setVgrow(table, Priority.ALWAYS);
			table.setFooterVisible(false);
			table.autosizeColumnsOnInitialization();
			MFXTableColumn<StudentReport> totalHoursColumn = new MFXTableColumn<>("Horas", true, Comparator.comparing(StudentReport::getTotalHours));
			MFXTableColumn<StudentReport> dayColumn = new MFXTableColumn<>("Día", true, Comparator.comparing(StudentReport::getDay));
			MFXTableColumn<StudentReport> teacherColumn = new MFXTableColumn<>("Profesor", true, Comparator.comparing(StudentReport::getTeacherCompleteName));
			MFXTableColumn<StudentReport> subjectColumn = new MFXTableColumn<>("Materia", true, Comparator.comparing(StudentReport::getSubjectName));
			MFXTableColumn<StudentReport> institutionColumn = new MFXTableColumn<>("Institución", true, Comparator.comparing(StudentReport::getInstitutionName));
			MFXTableColumn<StudentReport> totalColumn = new MFXTableColumn<>("Total ($)", true, Comparator.comparing(StudentReport::getTotal));
			MFXTableColumn<StudentReport> notifiedColumn = new MFXTableColumn<>("Notificado", false);
			MFXTableColumn<StudentReport> isPaidColumn = new MFXTableColumn<>("Pagado", false);
			
			totalHoursColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getTotalHours));
			dayColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getDay));
			teacherColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getTeacherCompleteName));
			subjectColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getSubjectName));
			institutionColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getInstitutionName));
			totalColumn.setRowCellFactory(device -> new MFXTableRowCell<>(StudentReport::getTotal));
			notifiedColumn.setRowCellFactory(device -> {
				MFXTableRowCell rc = new MFXTableRowCell<>(null, s->"");
				MFXCheckbox selectedCheck = new MFXCheckbox();
				selectedCheck.setSelected(device.isNotified());
				
				selectedCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
				    @Override
				    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			        	StudentReport student = table.getSelectionModel().getSelectedValue();
			        	LessonController lessonController = LessonController.getInstance();
			        	lessonController.setNotified(student, newValue);
			        	device.setNotified(newValue);
				    }
				});
				selectedCheck.setContentDisposition(ContentDisplay.GRAPHIC_ONLY);
				rc.setLeadingGraphic(selectedCheck);
				return rc;
			});
			isPaidColumn.setRowCellFactory(device -> {
				MFXTableRowCell rc = new MFXTableRowCell<>(null, s->"");
				MFXCheckbox selectedCheck = new MFXCheckbox();
				
				selectedCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
				    @Override
				    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				    	if(newValue) {
				    		Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado", "¿Desea marcar esta clase como pagada?.", gridPane);
				    		alert.getKey().addActions(
				    				Map.entry(new MFXButton("Confirmar"), e -> {
				    					alert.getValue().close();
							        	StudentReport student = table.getSelectionModel().getSelectedValue();
							        	LessonController lessonController = LessonController.getInstance();
							        	lessonController.makePayment(student);
							        	studentItems.remove(student);
				    				}),
				    				Map.entry(new MFXButton("Cancelar"), e -> {
				    					alert.getValue().close();
				    					selectedCheck.setSelected(false);
				    				})
				    		);
				    		alert.getValue().showDialog();
				        }
				    }
				});
				selectedCheck.setContentDisposition(ContentDisplay.GRAPHIC_ONLY);
				rc.setLeadingGraphic(selectedCheck);
				return rc;
			});
			
			table.getTableColumns().addAll(dayColumn, teacherColumn, subjectColumn, 
					institutionColumn, totalHoursColumn, totalColumn, notifiedColumn, isPaidColumn);
			
			table.getFilters().addAll(
				new DoubleFilter<>("Total ($)", StudentReport::getTotal),
				new IntegerFilter<>("Cantidad de horas", StudentReport::getTotalHours),
				//new LocalDateFilter<>("Dia", StudentReport::getDay),
				new StringFilter<>("Profesor", StudentReport::getTeacherCompleteName),
				new StringFilter<>("Materia", StudentReport::getSubjectName),
				new StringFilter<>("Institución", StudentReport::getInstitutionName)
			);
			table.setItems( studentItems );
			table.prefHeightProperty().bind(Bindings.size(studentItems).multiply(35).add(50));
			System.out.println(table.heightProperty());
			vBoxInternal.getChildren().addAll(hBox, table);
			vBox.getChildren().add(vBoxInternal);
		});
	}	
}
