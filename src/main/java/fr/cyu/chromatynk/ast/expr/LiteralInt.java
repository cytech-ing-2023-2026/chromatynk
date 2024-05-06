package fr.cyu.chromatynk.ast.expr;

public class LiteralInt implements Expr {

    private final int value;

    public LiteralInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
