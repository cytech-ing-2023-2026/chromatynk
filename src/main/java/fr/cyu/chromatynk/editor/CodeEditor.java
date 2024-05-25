package fr.cyu.chromatynk.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main JavaFX application loader for the code editor
 *
 * @author JordanViknar
 * @see CodeEditorController
 */
public class CodeEditor extends Application {

    private CodeEditorController controller;

    @Override
    @SuppressWarnings("exports")
    public void start(Stage primaryStage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf"), 15);
        Font.loadFont(getClass().getResourceAsStream("/fonts/JetBrainsMono-Bold.ttf"), 15);

        // Prepare and load the FXML file
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CodeEditor.fxml"));
        controller = new CodeEditorController(primaryStage);
        fxmlLoader.setController(controller);
        root = fxmlLoader.load();

        // Set the stage
        primaryStage.setTitle("Chromat'ynk");
        primaryStage.setScene(new Scene(root, 800, 700));
		primaryStage.setMinWidth(700);
		primaryStage.setMinHeight(700);
		// Add icon
		Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
		primaryStage.getIcons().add(icon);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        controller.shutdown();
    }

    /**
     * Launch the JavaFX application
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}