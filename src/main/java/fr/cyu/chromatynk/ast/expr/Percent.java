package fr.cyu.chromatynk.ast.expr;

public class Percent implements Expr {
    private final Expr value;

    public Percent(Expr value) {
        this.value = value;
    }

    public Expr getValue() {
        return value;
    }
}
