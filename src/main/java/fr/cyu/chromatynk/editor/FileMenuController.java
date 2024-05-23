package fr.cyu.chromatynk.editor;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The controller for handling file-related actions in the application.
 * It provides functionality to open and save files with content from/to a CodeArea.
 * 
 * @author JordanViknar
 * @see CodeEditorController
 */
public class FileMenuController {

	private final Stage primaryStage;
	private final CodeArea codeArea;

	/**
     * Constructs a {@link FileMenuController} with the given primary stage and code area.
     * 
     * @param primaryStage the primary stage of the application
     * @param codeArea the code area whose content needs to be loaded from or saved to a file
     */
	@SuppressWarnings("exports")
	public FileMenuController(Stage primaryStage, CodeArea codeArea) {
		this.primaryStage = primaryStage;
		this.codeArea = codeArea;
	}

	/**
	 * Opens a file dialog and loads the content of the selected file into the text area.
	 */
	public void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Ouvrir un fichier");

		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Chromat'ynk", "*.cty"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Autres fichiers", "*"));

		File selectedFile = fileChooser.showOpenDialog(primaryStage);
		if (selectedFile != null) {
			try {
				String content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
				codeArea.replaceText(content);
			} catch (IOException ex) {
				ex.printStackTrace();
				StringWriter sw = new StringWriter();
				ex.printStackTrace(new PrintWriter(sw));

				Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while reading the file.\n" + sw.getBuffer());
				alert.showAndWait();
			}
		}
	}

	/**
	 * Opens a file dialog and saves the content of the text area into the selected file.
	 */
	public void saveFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Enregistrer sous...");

		// Add a file extension filter for .cty files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers Chromat'ynk (*.cty)", "*.cty");
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
