package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;

public class TypeMismatchException extends EvalException {

    public TypeMismatchException(Range context, Type expected, Type actual) {
        super(context, "Type mismatch.\nExpected: " + expected + "\nGot: " + actual);
    }
}
