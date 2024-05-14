package fr.cyu.chromatynk.eval;

public class EvalContext {

    private final Scope scope;

    public EvalContext(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }
}
