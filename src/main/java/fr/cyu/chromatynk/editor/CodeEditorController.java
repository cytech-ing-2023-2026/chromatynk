package fr.cyu.chromatynk.editor;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

	// Tabs
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab plusTab;

	private int tabCounter = 1;
	private Map<Tab, String> tabContentMap = new HashMap<>(); // Map to store content of each tab

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

		// Handle tab changes
		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
			if (oldTab != null && oldTab != plusTab) {
				// Save content of the previous tab
				tabContentMap.put(oldTab, codeArea.getText());
			}
			if (newTab != null && newTab != plusTab) {
				// Load content of the new tab
				codeArea.setText(tabContentMap.getOrDefault(newTab, ""));
			}
			if (newTab == plusTab) {
				// Create a new tab
				tabCounter++;
				Tab newTabInstance = new Tab("Onglet " + tabCounter);
				newTabInstance.setClosable(true);

				// Add the new tab before the plus tab
				int plusTabIndex = tabPane.getTabs().indexOf(plusTab);
				tabPane.getTabs().add(plusTabIndex, newTabInstance);

				// Initialize content for the new tab
				tabContentMap.put(newTabInstance, "");

				// Add event handler to remove tab content when the tab is closed
				newTabInstance.setOnClosed(event -> tabContentMap.remove(newTabInstance));

				// Select the new tab
				tabPane.getSelectionModel().select(newTabInstance);
			}
		});
	}

	public void openFile() {fileMenuController.openFile();}
	public void saveFile() {fileMenuController.saveFile();}
	public void clearTextArea() {codeArea.clear();}
	public void quit() {primaryStage.close();}
}
