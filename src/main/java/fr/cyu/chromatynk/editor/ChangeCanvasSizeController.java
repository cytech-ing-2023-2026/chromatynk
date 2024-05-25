package fr.cyu.chromatynk.editor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChangeCanvasSizeController {
	@FXML
	private TextField widthField;
		
	@FXML
	private TextField heightField;

	private final CodeEditorController codeEditorController;
	private final Stage stage;

	@SuppressWarnings("exports")
	public ChangeCanvasSizeController(CodeEditorController codeEditorController, Stage stage) {
		this.codeEditorController = codeEditorController;
		this.stage = stage;
	}

	@FXML
	private void applyChanges() {
		try {
			// TODO : Prevent canvas size above 512x512
			codeEditorController.canvas.setWidth(Double.parseDouble(widthField.getText()));
			codeEditorController.canvas.setHeight(Double.parseDouble(heightField.getText()));
			codeEditorController.clearCanvas();
			stage.close();
		} catch (NumberFormatException error) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Entrée invalide");
			alert.setHeaderText(error.getMessage());
			alert.showAndWait();
		}
	}
}
