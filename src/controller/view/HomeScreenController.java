package controller.view;

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
import javafx.scene.layout.Pane;
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
	public void setContentPane(Pane pane) {
		contentPane.getChildren().setAll(pane);
	}
	private void initializeLoader() {
		MFXLoader loader = new MFXLoader();
		loader.addView(MFXLoaderBean.of("StudentScreen", getClass().getResource("/view/StudentScreen.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user-graduate", "Alumnos")).setDefaultRoot(true).get());
		loader.addView(MFXLoaderBean.of("TeacherScreen", getClass().getResource("/view/TeacherScreen.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user-tie", "Profesores")).get());
		loader.addView(MFXLoaderBean.of("SubjectScreen", getClass().getResource("/view/SubjectScreen.fxml")).setBeanToNodeMapper(() -> createToggle("fas-book", "Materias")).get());
		loader.addView(MFXLoaderBean.of("LessonScreen", getClass().getResource("/view/LessonScreen.fxml")).setBeanToNodeMapper(() -> createToggle("fas-users-between-lines", "Clase")).get());
		loader.addView(MFXLoaderBean.of("StudentReportScreen", getClass().getResource("/view/StudentReportScreen.fxml")).setBeanToNodeMapper(() -> createToggle("fas-chart-bar", "Reporte Alumnos")).get());
		loader.addView(MFXLoaderBean.of("TESTINGGG2", getClass().getResource("/view/testing2.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user-tie", "asdas")).get());
		loader.setOnLoadedAction(beans -> {
			List<ToggleButton> nodes = beans.stream()
					.map(bean -> {
						ToggleButton toggle = (ToggleButton) bean.getBeanToNodeMapper().get();
						toggle.setOnAction(event -> {
							contentPane.getChildren().setAll(bean.getRoot());
						});
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
