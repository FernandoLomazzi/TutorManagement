module TutorManagement {
    requires javafx.fxml;
	requires mfx.resources;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires MaterialFX;
	opens controller;
    exports application to javafx.graphics;
    exports controller to javafx.fxml;
}