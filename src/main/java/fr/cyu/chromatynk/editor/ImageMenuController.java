package fr.cyu.chromatynk.editor;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

/**
 * The controller for handling image-related actions in the application.
 * It provides functionality to save the content of the canvas to an image file.
 * 
 * @author JordanViknar
 * @see CodeEditorController
 */
public class ImageMenuController {

	private final Stage primaryStage;
	private final Canvas canvas;

	/**
	 * Constructs an {@link ImageMenuController} with the given primary stage and canvas.
	 * 
	 * @param primaryStage the primary stage of the application
	 * @param canvas the canvas whose content needs to be saved as an image
	 */
	@SuppressWarnings("exports")
	public ImageMenuController(Stage primaryStage, Canvas canvas) {
		this.primaryStage = primaryStage;
		this.canvas = canvas;
	}

	/**
	 * Opens a file dialog and saves the content of the canvas into the selected file.
	 * If the user does not provide a file extension, ".png" is appended to the filename.
	 */
	public void saveImage() {
		WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
		canvas.snapshot(null, writableImage);

		// Prevent saving blank images
		if (isImageBlank(writableImage)) {
			// Show alert window
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Canvas vide");
			alert.setHeaderText("Le dessin que vous essayez d'enregistrer est vide !");
			alert.showAndWait();
			return;
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Enregistrer le canvas sous...");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers image PNG", "*.png"));

		File selectedFile = fileChooser.showSaveDialog(primaryStage);
		if (selectedFile != null) {
			try {
				String filePath = selectedFile.getAbsolutePath();
				if (!filePath.endsWith(".png")) {
					filePath += ".png"; // Append .png extension if not provided
				}

				RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
				ImageIO.write(renderedImage, "png", new File(filePath));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Lazy method to check if a canvas is blank. However, it should work no matter what at least.
	 * 
	 * @param writableImage the image to check
	 * @return {@code true} if the image is blank
	 * @return {@code false} if the image is not blank
	 */
	private boolean isImageBlank(WritableImage writableImage) {
		PixelReader pixelReader = writableImage.getPixelReader();
		
		for (int y = 0; y < writableImage.getHeight(); y++) {
			for (int x = 0; x < writableImage.getWidth(); x++) {
				if (pixelReader.getArgb(x, y) != 0xFFFFFFFF) {
					return false; // Found a non-white pixel
				}
			}
		}
		return true; // All pixels are white
	}
}
