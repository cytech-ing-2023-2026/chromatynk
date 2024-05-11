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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class CodeEditor extends Application {

	@Override
	public void start(Stage primaryStage) {

		// Create text zone
		TextArea codeArea; {
			codeArea = new TextArea();
			codeArea.setPrefWidth(300);
	 		codeArea.setPrefHeight(200);
			codeArea.setWrapText(true);
		}

		// Execution / Deletion container
		HBox buttonBox; {
			Button deleteButton = new Button("Supprimer");
			deleteButton.setOnAction(e -> codeArea.clear()); // Action du bouton pour supprimer le texte
	
			Button validateButton = new Button("Exécuter");
			// This is where we run the draw system.

			buttonBox = new HBox(deleteButton, validateButton);
			buttonBox.setPadding(new Insets(10));
			buttonBox.setSpacing(10);
		}

		// Create drawing zone for the result
		Canvas canvas; {
			canvas = new Canvas(300, 200);
			// Configure white background
			GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
			graphicsContext.setFill(Color.WHITE);
			graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		}

		// Create drawing zone container
		BorderPane resultPane; {
			resultPane = new BorderPane();
			resultPane.setCenter(canvas);
		};

		// Création de la barre de menu
		MenuBar menuBar; {
			// Création du menu Fichier pour pouvoir enregistrer et ouvrir des images
			Menu fileMenu; {
				MenuItem openMenuItem = new MenuItem("Ouvrir");
				MenuItem saveAsPngMenuItem = new MenuItem("Enregistrer sous");
	
				// Action pour le menu Ouvrir (à completer)
	
				// Action pour le menu Enregistrer en PNG (A completer)
	
				fileMenu = new Menu("Fichier");
				// Ajout des éléments au menu Fichier
				fileMenu.getItems().addAll(openMenuItem, saveAsPngMenuItem);
			}
			menuBar = new MenuBar();
			menuBar.getMenus().add(fileMenu);
		}

		// Création de la scène
		Scene scene; {
			// Création du conteneur principal
			BorderPane root = new BorderPane();
			root.setLeft(codeArea);
			root.setBottom(buttonBox);
			root.setRight(resultPane);
			root.setTop(menuBar);

			scene = new Scene(root, 600, 400);
		}

		// Configuration de la scène et affichage de la fenêtre
		primaryStage.setTitle("Chromatynk");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
