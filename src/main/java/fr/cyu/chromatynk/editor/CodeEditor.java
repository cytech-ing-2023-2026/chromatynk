package fr.cyu.chromatynk.editor;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;
import javax.swing.SwingUtilities;




import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class CodeEditor extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Creating the text box for the code editor

        TextArea codeArea = new TextArea();
        codeArea.setPrefWidth(300);
        codeArea.setPrefHeight(200);
        codeArea.setWrapText(true); // Activer le retour à la ligne automatique

        // delete button
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(e -> codeArea.clear()); // Action du bouton pour supprimer le texte



        // valid button
        Button validateButton = new Button("Valider");
        //définir l'action (A completer)

        // Creating the container for the delete and validate button
        HBox buttonBox = new HBox(deleteButton, validateButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);

        // Creating the drawing area for the result
        Canvas canvas = new Canvas(300, 200);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        

        // Creating the container for the drawing area
        BorderPane resultPane = new BorderPane();
        resultPane.setCenter(canvas);

        // Creation of the File menu to be able to save and open images
        Menu fileMenu = new Menu("Fichier");
        MenuItem openMenuItem = new MenuItem("Ouvrir");
        MenuItem saveAsPngMenuItem = new MenuItem("Enregistrer sous");
        fileMenu.getItems().add(saveAsPngMenuItem);

        saveAsPngMenuItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            // Show Save As Dialog
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                try {
                    // Enregistrer le canevas en tant qu'image PNG
                    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                    canvas.snapshot(null, writableImage);
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                } catch (IOException ex) {
                    System.out.println("Erreur lors de l'enregistrement du fichier : " + ex.getMessage());
                }
            }
        });

        // Action pour le menu Ouvrir (à completer)




        // Adding items to the File menu
        fileMenu.getItems().addAll(openMenuItem, saveAsPngMenuItem);

        // Creating the menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);



        // Creating the main container
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setLeft(codeArea);
        root.setBottom(buttonBox);
        root.setRight(resultPane);
        root.setTop(menuBar);


        // Creating the scene
        Scene scene = new Scene(root, 600, 400);

        // Setting up the scene and displaying the window
        primaryStage.setTitle("Cromatynk");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
