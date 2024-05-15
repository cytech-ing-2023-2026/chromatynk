package fr.cyu.chromatynk.editor;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The controller for the file menu
 * @author JordanViknar
 * @see CodeEditor
 * @see CodeEditorController
 */
public class FileMenuController {

	private final Stage primaryStage;
	private final TextArea codeArea;

	@SuppressWarnings("exports")
	public FileMenuController(Stage primaryStage, TextArea codeArea) {
		this.primaryStage = primaryStage;
		this.codeArea = codeArea;
	}

	/**
	 * Opens a file dialog and loads the content of the selected file into the text area
	 */
	public void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Ouvrir un fichier");

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
	}

	/**
	 * Opens a file dialog and saves the content of the text area into the selected file
	 */
	public void saveFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Enregistrer sous...");

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
	}
}
