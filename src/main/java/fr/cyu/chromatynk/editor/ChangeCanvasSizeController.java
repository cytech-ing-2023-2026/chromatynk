package fr.cyu.chromatynk.editor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author JordanViknar
 * @see CodeEditorController
 * Controller for handling the change of canvas size in the editor.
 * This class provides the functionality to apply the user-specified width and height
 * to the canvas within the {@link CodeEditorController}.
 */
public class ChangeCanvasSizeController {
	@FXML
	private TextField widthField;
		
	@FXML
	private TextField heightField;

	private final CodeEditorController codeEditorController;
	private final Stage stage;

	/**
	 * Constructs a ChangeCanvasSizeController with the specified CodeEditorController and Stage.
	 *
	 * @param codeEditorController the CodeEditorController associated with this controller
	 * @param stage the stage for this controller's dialog
	 */
	@SuppressWarnings("exports")
	public ChangeCanvasSizeController(CodeEditorController codeEditorController, Stage stage) {
		this.codeEditorController = codeEditorController;
		this.stage = stage;
	}

	/**
	 * Applies the changes to the canvas size based on the values entered in the width and height fields.
	 * Validates the input and shows error alerts if the input is invalid.
	 */
	@FXML
	private void applyChanges() {
		try {
			Double width = Double.parseDouble(widthField.getText());
			Double height = Double.parseDouble(heightField.getText());

			if (width <= 0 || height <= 0) {
				throw new InvalidCanvasSizeException("Taille négative ou nulle", width, height);
			} else if (width > 1540 || height > 720) {
				throw new InvalidCanvasSizeException("Taille trop grande (maximum 1540x720)", width, height);
			}

			codeEditorController.canvas.setWidth(Double.parseDouble(widthField.getText()));
			codeEditorController.canvas.setHeight(Double.parseDouble(heightField.getText()));
			codeEditorController.clearCanvas();

			stage.close();
		} catch (NumberFormatException error) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Entrée invalide");
			alert.setHeaderText(error.getMessage());
			alert.showAndWait();
		} catch (InvalidCanvasSizeException error) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Taille demandée invalide");
			alert.setHeaderText(error.getMessage());
			alert.showAndWait();
		}
	}
}
