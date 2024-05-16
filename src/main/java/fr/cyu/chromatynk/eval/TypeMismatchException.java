package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;

/**
 * The given type does not match one of the expected ones.
 */
public class TypeMismatchException extends EvalException {

    /**
     * Create a new type mismatch exception.
     *
     * @param range the range where the error occurred
     * @param expected the expected type
     * @param actual the actual type
     */
    public TypeMismatchException(Range range, Type expected, Type actual) {
        super(range, "Type mismatch.\nExpected: " + expected + "\nGot: " + actual);
    }
}
