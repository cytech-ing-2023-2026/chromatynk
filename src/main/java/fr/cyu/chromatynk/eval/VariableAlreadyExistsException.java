package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.util.Range;

/**
 * Tried to declare an already-existing variable.
 */
public class VariableAlreadyExistsException extends EvalException {

    /**
     * Create a new "double declaration of a variable" exception.
     *
     * @param range the range where the error occurred
     * @param name  the name of the already-existing variable
     */
    public VariableAlreadyExistsException(Range range, String name) {
        super(range, "The variable " + name + " already exists");
    }
}