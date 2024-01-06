package application;
	
import java.text.MessageFormat;

import controller.view.HomeScreenController;
import io.github.palexdev.materialfx.i18n.I18N;
import io.github.palexdev.materialfx.i18n.Language;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		UserAgentBuilder.builder()
			.themes(JavaFXThemes.MODENA)
			.themes(MaterialFXStylesheets.forAssemble(true))
			.setDeploy(true)
			.setResolveAssets(true)
			.build()
			.setGlobal();
		I18N.setLanguage(Language.SPANISH);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeScreen.fxml"));
			loader.setControllerFactory(c -> new HomeScreenController(primaryStage));
			Pane root = loader.load();
			
			Scene scene = new Scene(root);
			scene.setFill(Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
