package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class HomeScreenController implements Initializable {
	private Stage stage;
	private double xOffset;
	private double yOffset;
	private ToggleGroup toggleGroup;

	@FXML
	private HBox windowHeader;
	@FXML
	private MFXFontIcon closeIcon;
	@FXML
	private MFXFontIcon minimizeIcon;
	@FXML
	private MFXFontIcon maximizeIcon;
	@FXML
	private AnchorPane rootPane;
	@FXML
	private MFXScrollPane scrollPane;
	@FXML
	private VBox navBar;
	@FXML
	private StackPane contentPane;
	@FXML
	private StackPane logoContainer;
	
	public HomeScreenController(Stage stage) {
		this.stage = stage;
		this.toggleGroup = new ToggleGroup();
		ToggleButtonsUtil.addAlwaysOneSelectedSupport(toggleGroup);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		closeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Platform.exit());
		minimizeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> ((Stage) rootPane.getScene().getWindow()).setIconified(true));
		maximizeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX(primaryScreenBounds.getMinX());
			stage.setY(primaryScreenBounds.getMinY());
			stage.setWidth(primaryScreenBounds.getWidth());
			stage.setHeight(primaryScreenBounds.getHeight());
		});
		windowHeader.setOnMousePressed(event -> {
			xOffset = stage.getX() - event.getScreenX();
			yOffset = stage.getY() - event.getScreenY();
		});
		windowHeader.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() + xOffset);
			stage.setY(event.getScreenY() + yOffset);
		});

		initializeLoader();

		ScrollUtils.addSmoothScrolling(scrollPane);

		// The only way to get a fucking smooth image in this shitty framework
		/*Image image = new Image(MFXDemoResourcesLoader.load("logo_alt.png"), 64, 64, true, true);
		ImageView logo = new ImageView(image);
		Circle clip = new Circle(30);
		clip.centerXProperty().bind(logo.layoutBoundsProperty().map(Bounds::getCenterX));
		clip.centerYProperty().bind(logo.layoutBoundsProperty().map(Bounds::getCenterY));
		logo.setClip(clip);
		logoContainer.getChildren().add(logo);*/
	}

	private void initializeLoader() {
		MFXLoader loader = new MFXLoader();
		loader.addView(MFXLoaderBean.of("StudentScreen", getClass().getResource("/view/StudentScreen.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user-graduate", "Alumnos")).setDefaultRoot(true).get());
		loader.addView(MFXLoaderBean.of("TeacherScreen", getClass().getResource("/view/TeacherScreen.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user-tie", "Profesores")).get());
		loader.addView(MFXLoaderBean.of("TESTINGGG2", getClass().getResource("/view/testing2.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user-tie", "asdas")).get());
		//loader.addView(MFXLoaderBean.of("BUTTONS", loadURL("fxml/Buttons.fxml")).setBeanToNodeMapper(() -> createToggle("fas-circle-dot", "Buttons")).setDefaultRoot(true).get());
		//loader.addView(MFXLoaderBean.of("CHECKS_RADIOS_TOGGLES", loadURL("fxml/ChecksRadiosToggles.fxml")).setBeanToNodeMapper(() -> createToggle("fas-toggle-on", "Checks, Radios, Toggles")).get());
		//loader.addView(MFXLoaderBean.of("COMBOS", loadURL("fxml/ComboBoxes.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "ComboBoxes")).get());
		//loader.addView(MFXLoaderBean.of("DIALOGS", loadURL("fxml/Dialogs.fxml")).setBeanToNodeMapper(() -> createToggle("fas-comments", "Dialogs")).setControllerFactory(c -> new DialogsController(stage)).get());
		//loader.addView(MFXLoaderBean.of("TEXT-FIELDS", loadURL("fxml/TextFields.fxml")).setBeanToNodeMapper(() -> createToggle("fas-italic", "Fields")).get());
		//loader.addView(MFXLoaderBean.of("LISTS", loadURL("fxml/ListViews.fxml")).setBeanToNodeMapper(() -> createToggle("fas-rectangle-list", "Lists")).get());
		//loader.addView(MFXLoaderBean.of("NOTIFICATIONS", loadURL("fxml/Notifications.fxml")).setBeanToNodeMapper(() -> createToggle("fas-bell", "Notifications")).setControllerFactory(c -> new NotificationsController(stage)).get());
		//loader.addView(MFXLoaderBean.of("PICKERS", loadURL("fxml/Pickers.fxml")).setBeanToNodeMapper(() -> createToggle("fas-calendar", "Pickers")).get());
		//loader.addView(MFXLoaderBean.of("PROGRESS", loadURL("fxml/Progress.fxml")).setBeanToNodeMapper(() -> createToggle("fas-bars-progress", "Progress")).get());
		//loader.addView(MFXLoaderBean.of("SCROLL-PANES", loadURL("fxml/ScrollPanes.fxml")).setBeanToNodeMapper(() -> createToggle("fas-bars-progress", "Scroll Panes", 90)).get());
		//loader.addView(MFXLoaderBean.of("SLIDERS", loadURL("fxml/Sliders.fxml")).setBeanToNodeMapper(() -> createToggle("fas-sliders", "Sliders")).get());
		//loader.addView(MFXLoaderBean.of("STEPPER", loadURL("fxml/Stepper.fxml")).setBeanToNodeMapper(() -> createToggle("fas-stairs", "Stepper")).get());
		//loader.addView(MFXLoaderBean.of("TABLES", loadURL("fxml/TableViews.fxml")).setBeanToNodeMapper(() -> createToggle("fas-table", "Tables")).get());
		//loader.addView(MFXLoaderBean.of("FONT-RESOURCES", loadURL("fxml/FontResources.fxml")).setBeanToNodeMapper(() -> createToggle("fas-icons", "Font Resources")).get());
		loader.setOnLoadedAction(beans -> {
			List<ToggleButton> nodes = beans.stream()
					.map(bean -> {
						ToggleButton toggle = (ToggleButton) bean.getBeanToNodeMapper().get();
						toggle.setOnAction(event -> contentPane.getChildren().setAll(bean.getRoot()));
						if (bean.isDefaultView()) {
							contentPane.getChildren().setAll(bean.getRoot());
							toggle.setSelected(true);
						}
						return toggle;
					})
					.toList();
			navBar.getChildren().setAll(nodes);
		});
		loader.start();
	}

	private ToggleButton createToggle(String icon, String text) {
		return createToggle(icon, text, 0);
	}

	private ToggleButton createToggle(String icon, String text, double rotate) {
		MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
		MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
		toggleNode.setAlignment(Pos.CENTER_LEFT);
		toggleNode.setMaxWidth(Double.MAX_VALUE);
		toggleNode.setToggleGroup(toggleGroup);
		if (rotate != 0) wrapper.getIcon().setRotate(rotate);
		return toggleNode;
	}
}
