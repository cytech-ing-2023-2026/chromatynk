package fr.cyu.chromatynk.ast.expr;

public class Not implements Expr {
    private final Expr value;

    public Not(Expr value) {
        this.value = value;
    }

    public Expr getValue() {
        return value;
    }
}
