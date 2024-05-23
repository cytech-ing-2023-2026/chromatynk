package fr.cyu.chromatynk.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    @Override
    @SuppressWarnings("exports")
    public void start(Stage primaryStage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf"), 15);
        Font.loadFont(getClass().getResourceAsStream("/fonts/JetBrainsMono-Bold.ttf"), 15);

        // Prepare and load the FXML file
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CodeEditor.fxml"));
        fxmlLoader.setController(new CodeEditorController(primaryStage));
        root = fxmlLoader.load();

        // Set the stage
        primaryStage.setTitle("Chromat'ynk");
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.show();
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