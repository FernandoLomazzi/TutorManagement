package com.tutormanagement.controller.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;

import com.tutormanagement.controller.model.LessonController;
import com.tutormanagement.model.TeacherReport;
import com.tutormanagement.utils.AlertManager;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.SortState;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import io.github.palexdev.materialfx.utils.others.observables.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class AllLessonController implements Initializable{

    @FXML
    private MFXButton addButton;

    @FXML
    private BorderPane borderPane;

    @FXML
    private MFXButton delButton;

    @FXML
    private MFXTableView<TeacherReport> lessonTable;

    @FXML
    private MFXButton modButton;

    @FXML
    private Label title;
    
    private ObservableList<TeacherReport> lessons;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupTable();
		lessonTable.autosizeColumnsOnInitialization();
		// Check this
		When.onChanged(lessonTable.tableRowFactoryProperty()).then((o, n) -> lessonTable.autosizeColumns()).listen();
		try {
			LessonController lessonController = LessonController.getInstance();
			lessons = FXCollections.observableArrayList(lessonController.getLessons());
			lessonTable.setItems(lessons);
		}catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), borderPane);
		}
	}
	
	void setupTable() {
		MFXTableColumn<TeacherReport> idColumn = new MFXTableColumn<>("ID", true,
				Comparator.comparing(TeacherReport::getLessonID));
		MFXTableColumn<TeacherReport> dayColumn = new MFXTableColumn<>("Día", true,
				Comparator.comparing(TeacherReport::getDay));
		MFXTableColumn<TeacherReport> teacherColumn = new MFXTableColumn<>("Profesor", true,
				Comparator.comparing(TeacherReport::getTeacherFullName));
		MFXTableColumn<TeacherReport> subjectColumn = new MFXTableColumn<>("Materia", false,
				Comparator.comparing(TeacherReport::getSubjectName));
		MFXTableColumn<TeacherReport> studentsColumn = new MFXTableColumn<>("Alumnos", false,
				Comparator.comparing(TeacherReport::getStudentsName));

		idColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getLessonID));
		dayColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getDay));
		teacherColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getTeacherFullName));
		subjectColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getSubjectName));
		studentsColumn.setRowCellFactory(device -> new MFXTableRowCell<>(TeacherReport::getStudentsName));
		
		idColumn.setSortState(SortState.DESCENDING);
		
		lessonTable.getTableColumns().addAll(idColumn, dayColumn, teacherColumn, subjectColumn, studentsColumn);

		lessonTable.getFilters().addAll(new IntegerFilter<>("ID", TeacherReport::getLessonID),
				new StringFilter<>("Profesor", TeacherReport::getTeacherFullName),
				new StringFilter<>("Materia", TeacherReport::getSubjectName),
				new StringFilter<>("Alumnos", TeacherReport::getStudentsName));
	}
    @FXML
    void createLesson(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LessonScreen.fxml"));
		try {
			Pane root = loader.load();
			((StackPane) borderPane.getParent()).getChildren().setAll(root);
		} catch (IOException e) {
			AlertManager.createError("ERROR",
					"Hubo un error al intentar cargar la pantalla de creación de clases\n" + e.getMessage(), borderPane);
		}
    }

    @FXML
    void deleteLesson(ActionEvent ev) {
		TeacherReport lesson = lessonTable.getSelectionModel().getSelectedValue();
		if (lesson != null) {
			Pair<MFXGenericDialog, MFXStageDialog> alert = AlertManager.createWarning("Cuidado",
					"¿Desea eliminar esta clase (ID: " + lesson.getLessonID() + ") del sistema? Una vez realizado será irrevertible.", borderPane);
			alert.getKey().addActions(Map.entry(new MFXButton("Confirmar"), event -> {
				alert.getValue().close();
				try {
					LessonController lessonController = LessonController.getInstance();
					lessonController.deleteLesson(lesson.getLessonID());
					lessons.remove(lesson);
				} catch (SQLException e) {
					AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), borderPane);
				}
			}), Map.entry(new MFXButton("Cancelar"), e -> alert.getValue().close()));
			alert.getValue().showDialog();
		} else {
			AlertManager.createError("Error", "Debe seleccionar una clase antes de eliminarla", borderPane);
		}
    }

    @FXML
    void modifyLesson(ActionEvent event) {
    	TeacherReport lesson = lessonTable.getSelectionModel().getSelectedValue();
    	if(lesson != null) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyLessonScreen.fxml"));
    		try {
    			Pane root = loader.load();
    			ModifyLessonScreenController controller = loader.getController();
    			controller.initializeFields(lesson);
    			Stage newStage = new Stage();
    	    	Scene scene = new Scene(root);

    			scene.setFill(Color.TRANSPARENT);
    			newStage.initModality(Modality.APPLICATION_MODAL);
    			newStage.initStyle(StageStyle.TRANSPARENT);
    			newStage.setScene(scene);
    			newStage.showAndWait();
    			lessonTable.update();
    		} catch (IOException e) {
    			AlertManager.createError("ERROR",
    					"Hubo un error al intentar cargar la pantalla de modificación de clases\n" + e.getMessage(), borderPane);
    		}
    	}
    	else {
			AlertManager.createError("Error", "Debe seleccionar una clase antes de modificarla", borderPane);
    	}
    }


}
