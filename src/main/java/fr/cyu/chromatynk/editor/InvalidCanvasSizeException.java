package fr.cyu.chromatynk.editor;

public class InvalidCanvasSizeException extends Exception {
	public InvalidCanvasSizeException(String message, Double width, Double height) {
		super(message + " (" + width + "x" + height + ")");
	}
}
