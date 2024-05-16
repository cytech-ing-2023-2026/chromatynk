package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * The given type does not match one of the expected ones.
 */
public class TypeMismatchException extends EvalException {

    /**
     * Create a new type mismatch exception.
     *
     * @param range the range where the error occurred
     * @param expected the expected types
     * @param actual the actual type
     */
    public TypeMismatchException(Range range, Set<Type> expected, Type actual) {
        super(range, "Type mismatch.\nExpected: " + expected.stream().map(Type::getName).collect(Collectors.joining(", ")) + "\nGot: " + actual);
    }
}
