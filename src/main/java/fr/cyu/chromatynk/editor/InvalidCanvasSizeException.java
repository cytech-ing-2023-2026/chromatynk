package fr.cyu.chromatynk.editor;

/**
 * @author JordanViknar
 * @see ChangeCanvasSizeController
 * 
 * Thrown when the user tries to set an invalid canvas size, that is either negative or too large.
 */
public class InvalidCanvasSizeException extends Exception {
	public InvalidCanvasSizeException(String message, Double width, Double height) {
		super(message + " (" + width + "x" + height + ")");
	}
}
