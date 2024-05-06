package fr.cyu.chromatynk.ast.expr;

public class Negation implements Expr {
    private final Expr value;

    public Negation(Expr value) {
        this.value = value;
    }

    public Expr getValue() {
        return value;
    }
}
