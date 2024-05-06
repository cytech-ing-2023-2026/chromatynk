package fr.cyu.chromatynk.ast.expr;

public class LiteralBool implements Expr {
    private final boolean value;

    public LiteralBool(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
