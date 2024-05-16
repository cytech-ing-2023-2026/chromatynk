package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.util.Range;

/**
 * An exception occurring while evaluating the AST.
 */
public class EvalException extends Exception {

    private final Range range;

    /**
     * Create a new evaluation exception.
     *
     * @param range the range where the error occurred
     * @param message the error message
     */
    public EvalException(Range range, String message) {
        super(message);
        this.range = range;
    }

    /**
     * Get the range where the error occurred.
     */
    public Range getRange() {
        return range;
    }
}
