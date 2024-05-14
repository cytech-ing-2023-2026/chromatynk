package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.util.Range;

public class VariableAlreadyExistsException extends EvalException {
    public VariableAlreadyExistsException(Range range, String name) {
        super(range,"The variable " + name + " already exists");
    }
}