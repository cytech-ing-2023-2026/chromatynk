package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.util.Range;

/**
 * The referenced variable does not exist.
 */
public class MissingVariableException extends EvalException {

    /**
     * Create a new "missing variable" exception.
     *
     * @param range the range where the error occurred
     * @param name the name of the missing variable
     */
    public MissingVariableException(Range range, String name) {
        super(range, "No such variable: " + name);
    }
}
