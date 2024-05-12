package fr.cyu.chromatynk.editor;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class CodeEditorController {
	@FXML
	private TextArea codeArea;
	@FXML
	private Canvas canvas;
	@FXML
	private Label infoLabel;
	@FXML
	private Label statusLabel;
	@FXML
	private HBox bottomBar;

	private final Stage primaryStage;
	private FileMenuController fileMenuController;

	@SuppressWarnings("exports")
	public CodeEditorController(Stage primaryStage) {this.primaryStage = primaryStage;}
	
	public void initialize() {
		this.fileMenuController = new FileMenuController(primaryStage, codeArea);

		// Set background color of the canvas
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// Makes the bottom bar's background slightly darker
		bottomBar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.07);");
	}

	public void openFile() {fileMenuController.openFile();}
	public void saveFile() {fileMenuController.saveFile();}
	public void clearTextArea() {codeArea.clear();}
}
