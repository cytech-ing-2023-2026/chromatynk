package fr.cyu.chromatynk.eval;

public class EvalException extends Exception {

    private final EvalContext context;

    public EvalException(EvalContext context, String message) {
        super(message);
        this.context = context;
    }

    public EvalContext getContext() {
        return context;
    }
}
