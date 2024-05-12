package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;

public class TypeMismatchException extends EvalException {

    public TypeMismatchException(EvalContext context, Type expected, Type actual) {
        super(context, "Type mismatch.\nExpected: " + expected + "\nGot: " + actual);
    }
}
