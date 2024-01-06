module TutorManagement {
    requires javafx.fxml;
	requires mfx.resources;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires MaterialFX;
	requires java.sql;
	requires javafx.base;
	opens controller.view;
    exports application to javafx.graphics;
    exports controller.view to javafx.fxml;
}