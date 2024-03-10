package com.tutormanagement.controller.view;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

import com.tutormanagement.controller.model.StatisticController;
import com.tutormanagement.model.EducationLevel;
import com.tutormanagement.utils.AlertManager;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

public class StatsScreenController implements Initializable {
    @FXML
    private GridPane gridPane;
	@FXML
	private MFXComboBox<StatType> typeField;
	@FXML
	private MFXDatePicker beginDateField;
	@FXML
	private MFXDatePicker endDateField;
	@FXML
	private MFXButton searchButton;
	@FXML
	private StackPane stackPane;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		typeField.setConverter(new StringConverter<StatType>() {
			@Override
			public String toString(StatType type) {
				return type==null?"":type.getType();
			}
			@Override
            public StatType fromString(String s) {
                for (StatType statType : StatType.values()) {
                    if (statType.getType().equals(s)) {
                        return statType;
                    }
                }
                return null;
            }
		});
		
		typeField.setItems(FXCollections.observableArrayList(StatType.values()));
		
		searchButton.setGraphic(new MFXIconWrapper("fas-magnifying-glass", 24, 32));
		searchButton.setOnAction(ev -> {
			StatType type = typeField.getSelectedItem();
			if(type==null) {
				AlertManager.createError("ERROR","Seleccione un tipo de estadística a generar", gridPane);
				return;
			}

			LocalDate beginDate = beginDateField.getValue(), endDate = endDateField.getValue();
			if(beginDate==null || endDate==null || beginDate.isAfter(endDate)){
				AlertManager.createError("ERROR","Seleccione fechas de inicio y de fin válidas", gridPane);
				return;
			}
			switch(type) {
			case MONEY:
				createMoneyStats(beginDate, endDate);
				break;
			case TOTAL_HOURS:
				createTotalHoursStats(beginDate, endDate);
				break;
			/*default:
				AlertManager.createError("ERROR","Seleccione un tipo de estadística a generar", gridPane);
				return;*/
			}
		});
	}
	private void createTotalHoursStats(LocalDate beginDate, LocalDate endDate) {
		HBox statHbox = new HBox(20);
		stackPane.getChildren().setAll(statHbox);
		statHbox.getChildren().setAll(new Label("En progreso"));
	}
	private BarChart<String, Number> createBarChartMoneyStats(LocalDate beginDate, LocalDate endDate){
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Dinero por nivel"); 
        yAxis.setLabel("Dinero ($)");
        
        Double totalIncome, totalTeacherIncome;
    	Series<String, Number> serieTotal = new Series<String, Number>();
    	serieTotal.setName("Ingreso Total");
    	Map<EducationLevel, Double> netInc;
    	try {
        	StatisticController statController = StatisticController.getInstance();
        	Map<EducationLevel, Double> totalInc = statController.getTotalIncomePerLevel(beginDate, endDate);
			totalIncome = totalInc.values().stream().mapToDouble(Double::doubleValue).sum();
        	totalInc.forEach((level, total) -> {
				serieTotal.getData().add(new Data<>(level.toString(), total));
			});
        	
			netInc = totalInc;
		} catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
			return null;
		}
    	
    	Series<String, Number> serieTeacher = new Series<String, Number>();
    	serieTeacher.setName("Ingreso Profesores");

    	try {
        	StatisticController statController = StatisticController.getInstance();
			Map<EducationLevel, Double> teacherInc = statController.getTeacherIncomePerLevel(beginDate, endDate);
			totalTeacherIncome = teacherInc.values().stream().mapToDouble(Double::doubleValue).sum();
			teacherInc.forEach((level, total) -> {
				serieTeacher.getData().add(new Data<>(level.toString(), total));
				if(netInc.containsKey(level)) {
					netInc.put(level, netInc.get(level) - total);
				}
				else {
					netInc.put(level, -total);
				}
			});
		} catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
			return null;
		}
    	Series<String, Number> serieNet = new Series<String, Number>();       
    	serieNet.setName("Ingreso Neto");
    	netInc.forEach((level, total) -> 
    		serieNet.getData().add(new Data<>(level.toString(), total)));
    	
    	serieTotal.getData().add(new Data<>("Total", totalIncome));
    	serieTeacher.getData().add(new Data<>("Total", totalTeacherIncome));
    	serieNet.getData().add(new Data<>("Total", totalIncome-totalTeacherIncome));
    	
    	bc.getData().addAll(serieTotal, serieTeacher, serieNet);
		bc.getData().forEach(series -> series.getData().forEach(item -> {
	        Tooltip tooltip = new Tooltip(item.getXValue()+"\n"+series.getName() + ": " + item.getYValue());
	        // previsional
	        tooltip.setStyle("-fx-font-size: 20");
	        Tooltip.install(item.getNode(), tooltip);	        
		}));
		return bc;
	}
	private LineChart<String, Number> createLineChartMoneyStats(LocalDate beginDate, LocalDate endDate){
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<String, Number> lc = new LineChart<String, Number>(xAxis, yAxis);
        lc.setTitle("Dinero en el tiempo");
        yAxis.setLabel("Dinero ($)");
        //Series<LocalDate, Number> serieTotal
        Series<String, Number> serieTotal = new Series<String, Number>();
    	serieTotal.setName("Ingreso por clase");
    	try {
        	StatisticController statController = StatisticController.getInstance();
			Map<LocalDate, Double> totalInc = statController.getTotalIncomePerTime(beginDate, endDate);
			ObservableList<String> categories = FXCollections.observableArrayList();
			totalInc.forEach((day, total) -> {
				categories.add(day.toString());
				serieTotal.getData().add(new Data<>(day.toString(), total));
			});
			Collections.sort(categories);
			//xAxis.setAutoRanging(true);
			xAxis.setCategories(categories);
		} catch (SQLException e) {
			AlertManager.createError("ERROR " + e.getErrorCode(), e.getMessage(), gridPane);
			return null;
		}
    	lc.getData().add(serieTotal);
    	lc.getData().forEach(series -> series.getData().forEach(item -> {
	        Tooltip tooltip = new Tooltip(series.getName() + "(" + item.getXValue() + "): " + item.getYValue());
	        // previsional
	        tooltip.setStyle("-fx-font-size: 20");
	        Tooltip.install(item.getNode(), tooltip);	        
		}));
    	return lc;
	}
	private void createMoneyStats(LocalDate beginDate, LocalDate endDate) {
		final LineChart<String, Number> lc = createLineChartMoneyStats(beginDate, endDate);
        final BarChart<String,Number> bc = createBarChartMoneyStats(beginDate, endDate);
		HBox statHbox = new HBox(20);
		stackPane.getChildren().setAll(statHbox);
        statHbox.getChildren().setAll(bc, lc);
        //statHbox.getChildren().add(bc);
	}
	private enum StatType{
		MONEY("Dinero"), TOTAL_HOURS("Horas");
		private final String type;

		private StatType(String type) {
			this.type = type;
		}
		public String getType() {
			return type;
		}
	}
}
