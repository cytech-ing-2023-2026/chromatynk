package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.util.Range;

public class MissingVariableException extends EvalException {

    public MissingVariableException(Range range, String variable) {
        super(range, "No such variable: " + variable);
    }
}
