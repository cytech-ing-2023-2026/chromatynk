package fr.cyu.chromatynk.eval;

public class MissingVariableException extends EvalException {

    public MissingVariableException(EvalContext context, String variable) {
        super(context, "No such variable: " + variable);
    }
}
