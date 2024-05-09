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

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class CodeEditor extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Création de la zone de texte pour l'éditeur de code

        TextArea codeArea = new TextArea();
        codeArea.setPrefWidth(300);
        codeArea.setPrefHeight(200);
        codeArea.setWrapText(true); // Activer le retour à la ligne automatique

        // Création du bouton supprimer
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(e -> codeArea.clear()); // Action du bouton pour supprimer le texte



        // Création boutton "Valider"
        Button validateButton = new Button("Valider");
        //définir l'action (A completer)

        // Création du conteneur pour le bouton supprimer et valider
        HBox buttonBox = new HBox(deleteButton, validateButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);

        // Création de la zone de dessin pour le résultat
        Canvas canvas = new Canvas(300, 200);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        // Création du conteneur pour la zone de dessin
        BorderPane resultPane = new BorderPane();
        resultPane.setCenter(canvas);

        // Création du menu Fichier pour pouvoir enregistrer et ouvrir des images
        Menu fileMenu = new Menu("Fichier");
        MenuItem openMenuItem = new MenuItem("Ouvrir");
        MenuItem saveAsPngMenuItem = new MenuItem("Enregistrer sous");

        // Action pour le menu Ouvrir (à completer)


        // Action pour le menu Enregistrer en PNG (A completer)


        // Ajout des éléments au menu Fichier
        fileMenu.getItems().addAll(openMenuItem, saveAsPngMenuItem);

        // Création de la barre de menu
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);



        // Création du conteneur principal
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setLeft(codeArea);
        root.setBottom(buttonBox);
        root.setRight(resultPane);
        root.setTop(menuBar);


        // Création de la scène
        Scene scene = new Scene(root, 600, 400);

        // Configuration de la scène et affichage de la fenêtre
        primaryStage.setTitle("Cromatynk");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
