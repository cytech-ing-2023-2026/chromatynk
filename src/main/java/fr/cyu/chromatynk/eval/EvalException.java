package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ChromatynkException;
import fr.cyu.chromatynk.util.Range;

/**
 * An exception occurring while evaluating the AST.
 */
public class EvalException extends ChromatynkException {

    /**
     * Create a new evaluation exception.
     *
     * @param range the range where the error occurred
     * @param message the error message
     */
    public EvalException(Range range, String message) {
        super(range, message);
    }

}
