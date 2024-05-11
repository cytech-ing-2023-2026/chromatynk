package fr.cyu.chromatynk.editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class CodeEditor extends Application {

	@Override
	public void start(@SuppressWarnings("exports") Stage primaryStage) {
		// Interactive elements
		TextArea codeArea = new TextArea();
		Canvas canvas = new Canvas(300, 200);

		// Create scene
		Scene scene; {
			// Create main container
			BorderPane root; {
				root = new BorderPane();

				// Create menu bar
				MenuBar menuBar; {
					// Create File menu to open and save drawing instructions
					Menu fileMenu; {
						MenuItem openMenuItem = new MenuItem("Ouvrir");
						MenuItem saveFileMenuItem = new MenuItem("Enregistrer sous");
			
						// The actions for both menu items should be implemented here
						// Open menu item action
						openMenuItem.setOnAction(e -> {
							FileChooser fileChooser = new FileChooser();
							fileChooser.setTitle("Open File");
							
							// Add a file extension filter for .cty files
							FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers Chromatynk (*.cty)", "*.cty");
							fileChooser.getExtensionFilters().add(extFilter);
							
							File selectedFile = fileChooser.showOpenDialog(primaryStage);
							if (selectedFile != null) {
								try {
									String content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
									codeArea.setText(content);
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}
						});
				
						// Save menu item action
						saveFileMenuItem.setOnAction(e -> {
							FileChooser fileChooser = new FileChooser();
							fileChooser.setTitle("Save File");
							
							// Add a file extension filter for .cty files
							FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers Chromatynk (*.cty)", "*.cty");
							fileChooser.getExtensionFilters().add(extFilter);
							
							File selectedFile = fileChooser.showSaveDialog(primaryStage);
							if (selectedFile != null) {
								try {
									String filePath = selectedFile.getAbsolutePath();
									if (!filePath.endsWith(".cty")) {
										filePath += ".cty"; // Append .cty extension if not provided
									}
									Files.write(Paths.get(filePath), codeArea.getText().getBytes());
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}
						});
			
						fileMenu = new Menu("Fichier");
						fileMenu.getItems().addAll(openMenuItem, saveFileMenuItem);
					}
					// Create Image menu to save the drawing as an image
					Menu imageMenu; {
						MenuItem saveImageMenuItem = new MenuItem("Enregistrer sous");
						imageMenu = new Menu("Image");
						imageMenu.getItems().addAll(saveImageMenuItem);
					}
					// Create about menu
					Menu helpMenu; {
						MenuItem helpMenuItem = new MenuItem("À propos");
						helpMenu = new Menu("Aide");
						helpMenu.getItems().add(helpMenuItem);
					}

					menuBar = new MenuBar();
					menuBar.getMenus().addAll(fileMenu, imageMenu, helpMenu);
				}
				root.setTop(menuBar);
				
				HBox mainArea; {
					VBox userControlArea; {
						// Configure text zone
						{
							codeArea.setPrefWidth(300);
							codeArea.setPrefHeight(300);
							codeArea.setWrapText(true);
							VBox.setVgrow(codeArea, Priority.ALWAYS);
						}

						// Execution / Deletion container
						HBox buttonBox; {
							Button validateButton = new Button("Exécuter");
							// This is where we run the draw system.

							Button deleteButton = new Button("Supprimer");
							deleteButton.setOnAction(e -> codeArea.clear());

							buttonBox = new HBox(validateButton, deleteButton);
							buttonBox.setPadding(new Insets(10));
							buttonBox.setSpacing(10);
						}
						userControlArea = new VBox(codeArea, buttonBox);
					}

					// Create drawing zone container
					BorderPane resultPane; {
						// Configure drawing zone
						{
							// Configure white background
							GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
							graphicsContext.setFill(Color.WHITE);
							graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
						}
						resultPane = new BorderPane();
						resultPane.setCenter(canvas);
						HBox.setHgrow(resultPane, Priority.ALWAYS);
					};
					mainArea = new HBox(userControlArea, resultPane);
					mainArea.setSpacing(10);
					mainArea.setPadding(new Insets(10));
				}
				root.setCenter(mainArea);

				// Set bottom status bar
				HBox statusBox; {
					Label infoLabel = new Label("INFO - Message d'information utile...");

					Region spacer = new Region();
					HBox.setHgrow(spacer, Priority.ALWAYS); // allow it to grow horizontally

					Label statusLabel = new Label("Dessin en cours...");

					statusBox = new HBox(infoLabel, spacer, statusLabel);
					statusBox.setPadding(new Insets(10));
					statusBox.setSpacing(10);
				}
				root.setBottom(statusBox);
			}
			scene = new Scene(root, 600, 400);
		}

		primaryStage.setTitle("Chromatynk");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
