package com.tutormanagement.utils;

import java.util.List;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import javafx.css.PseudoClass;

public class ValidatorManager {
	private ValidatorManager() {

	}

	public static void notNullConstraint(MFXTextField field) {
		field.getValidator().constraint("NotNull", field.textProperty().isNotEmpty());
		field.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				field.pseudoClassStateChanged(PseudoClass.getPseudoClass("invalid"), false);
			}
		});
		field.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue && !newValue) {
				List<Constraint> constraints = field.validate();
				if (!constraints.isEmpty()) {
					field.pseudoClassStateChanged(PseudoClass.getPseudoClass("invalid"), true);
					// validationLabel
				}
			}
		});
	}

	public static void numberConstraint(MFXTextField field) {

	}
}
