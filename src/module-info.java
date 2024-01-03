module TutorManagement {
    requires javafx.fxml;
	requires mfx.resources;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires MaterialFX;
	requires java.sql;
	opens controller.view;
    exports application to javafx.graphics;
    exports controller to javafx.fxml;
}