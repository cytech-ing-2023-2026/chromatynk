package fr.cyu.chromatynk.editor;

import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AboutWindowController {
	@FXML
	private ImageView iconImageView;

	private final Stage stage;

	@SuppressWarnings("exports")
	public AboutWindowController(Stage stage) {
		this.stage = stage;
	}

	@FXML
    public void initialize() {
        // Load the icon
        Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
        iconImageView.setImage(icon);
    }

	@FXML
	private void closeWindow() {
		stage.close();
	}
}
