package fr.cyu.chromatynk.editor;

import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The controller for the About window.
 * @author JordanViknar
 * @see CodeEditorController
 */
public class AboutWindowController {
	@FXML
	private ImageView iconImageView;

	private final Stage stage;

	/**
	 * Initializes the About window controller.
	 * @param stage the stage to use
	 */
	@SuppressWarnings("exports")
	public AboutWindowController(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Initializes the About window, setting the icon image.
	 */
	@FXML
    public void initialize() {
        // Load the icon
        Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
        iconImageView.setImage(icon);
    }

	/**
	 * Closes the About window.
	 */
	@FXML
	private void closeWindow() {
		stage.close();
	}
}
