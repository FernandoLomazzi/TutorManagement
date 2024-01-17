package com.tutormanagement.utils;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.css.PseudoClass;

public class MaterialFXManager {
	public static void clearField(MFXTextField textField) {
		textField.clear();
		textField.pseudoClassStateChanged(PseudoClass.getPseudoClass("floating"), false);
	}

	/*
	 * public static void clearAllFields(Collection<? extends MFXTextField>
	 * textFields) { textFields.forEach(tf -> MaterialFXManager.clearField(tf)); }
	 */
	public static void clearAllFields(MFXTextField... textFields) {
		for (MFXTextField tf : textFields) {
			MaterialFXManager.clearField(tf);
			if (tf instanceof MFXDatePicker)
				((MFXDatePicker) tf).setValue(null);
			if (tf instanceof MFXComboBox)
				((MFXComboBox) tf).clearSelection();
		}
	}
}
