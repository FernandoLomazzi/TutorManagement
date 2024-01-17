package com.tutormanagement.controller.view;

import java.net.URL;
import java.util.ResourceBundle;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

public class StatsScreenController implements Initializable {
	@FXML
	private MFXComboBox typeField;
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
		searchButton.setGraphic(new MFXIconWrapper("fas-magnifying-glass", 24, 32));
	}

}
