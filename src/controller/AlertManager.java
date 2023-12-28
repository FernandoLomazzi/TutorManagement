package controller;

import java.util.Map;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.util.Pair;

public class AlertManager {
	private AlertManager() {
		;
	}
	private static Pair<MFXGenericDialog, MFXStageDialog> createAlert(String title, String contentText, AlertType alertType, Pane owner) {
		MFXGenericDialog error = new MFXGenericDialog(title, contentText);
		error.setAlwaysOnTop(true);
		MFXFontIcon icon;
		MFXStageDialog errorStage = MFXGenericDialogBuilder.build(error)
				.toStageDialogBuilder()
				.initModality(Modality.APPLICATION_MODAL)
				.setDraggable(true)
				.setOwnerNode(owner)
				.setScrimPriority(ScrimPriority.WINDOW)
				.setScrimOwner(true)
				.get();
		switch(alertType) {
			case ERROR:
				icon = new MFXFontIcon("fas-circle-xmark", 18);
				error.getStyleClass().add("mfx-error-dialog");
				error.addActions(Map.entry(new MFXButton("Confirmar"), event -> errorStage.close()));
				break;
			case INFORMATION:
				icon = new MFXFontIcon("fas-circle-info", 18);
				error.getStyleClass().add("mfx-info-dialog");
				error.addActions(Map.entry(new MFXButton("Confirmar"), event -> errorStage.close()));
				break;
			case WARNING:
				icon = new MFXFontIcon("fas-circle-exclamation", 18);
				error.getStyleClass().add("mfx-warn-dialog");

				break;
			default:
				icon = null;
				error.addActions(Map.entry(new MFXButton("Confirmar"), event -> errorStage.close()));
				break;
		}
		error.setHeaderIcon(icon);
		errorStage.setOwnerNode(owner);
		return new Pair<>(error, errorStage);
	}
	public static void createError(String title, String contentText, Pane owner) {
		createAlert(title, contentText, Alert.AlertType.ERROR, owner).getValue().showDialog();
	}
	public static void createInformation(String title, String contentText, Pane owner) {
		createAlert(title, contentText, Alert.AlertType.INFORMATION, owner).getValue().showDialog();
	}
	public static Pair<MFXGenericDialog, MFXStageDialog> createWarning(String title, String contentText, Pane owner) {
		return createAlert(title, contentText, Alert.AlertType.WARNING, owner);
	}
	public static void genericAlert(Pane owner) {
		createAlert("Alerta", "Se ha producido un error inesperado", Alert.AlertType.NONE, owner).getValue().showDialog();
	}
}
