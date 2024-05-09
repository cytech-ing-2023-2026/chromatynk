package fr.cyu.chromatynk.eval;

public class VariableAlreadyExistsException extends EvalException {
    public VariableAlreadyExistsException(EvalContext context, String name) {
        super(context,"The variable " + name + " already exists");
    }
}