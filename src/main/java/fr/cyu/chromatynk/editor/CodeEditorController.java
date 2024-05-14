package fr.cyu.chromatynk.editor;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

/**
 * The controller for the code editor
 * @author JordanViknar
 */
public class CodeEditorController {
	// The 2 main elements
	@FXML
	private TextArea codeArea;
	@FXML
	private Canvas canvas;

	// Bottom bar
	@FXML
	private Label infoLabel;
	@FXML
	private Label statusLabel;
	@FXML
	private HBox bottomBar;

	// Step-by-step mode
	@FXML
	private CheckMenuItem stepByStepCheckbox;
	@FXML
	private HBox stepByStepControls;

	private final Stage primaryStage;
	private FileMenuController fileMenuController;

	@SuppressWarnings("exports")
	public CodeEditorController(Stage primaryStage) {this.primaryStage = primaryStage;}
	
	/**
	 * This function is used to setup the UI elements of the code editor Java-side.
	 * Basically : it's for things we cannot do in FXML.
	 */
	public void initialize() {
		this.fileMenuController = new FileMenuController(primaryStage, codeArea);

		// Set background color of the canvas
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// Makes the bottom bar's background slightly darker
		bottomBar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.07);");

		// Only show relevant UI when in step-by-step mode
		stepByStepControls.setVisible(stepByStepCheckbox.isSelected());
		stepByStepCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			stepByStepControls.setVisible(newValue);
		});
	}

	public void openFile() {fileMenuController.openFile();}
	public void saveFile() {fileMenuController.saveFile();}
	public void clearTextArea() {codeArea.clear();}
	public void quit() {primaryStage.close();}
}
