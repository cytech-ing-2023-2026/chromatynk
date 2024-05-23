package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.util.Range;

public class InvalidExpressionException extends EvalException {
    /**
     * Create a new evaluation exception.
     *
     * @param range   the range where the error occurred
     * @param message the error message
     */
    public InvalidExpressionException(Range range, String message) {
        super(range, "Invalid value: " + message);
    }
}
