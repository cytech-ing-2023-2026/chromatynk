package fr.cyu.chromatynk.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CodeEditor extends Application {

	@Override
	@SuppressWarnings("exports")
	public void start(Stage primaryStage) throws IOException {
	   	// Prepare and load the FXML file
		Parent root; {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CodeEditor.fxml"));
			fxmlLoader.setController(new CodeEditorController(primaryStage));
			root = fxmlLoader.load();
		};

		// Set the stage
		primaryStage.setTitle("Chromatynk");
		primaryStage.setScene(new Scene(root, 800, 500));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}