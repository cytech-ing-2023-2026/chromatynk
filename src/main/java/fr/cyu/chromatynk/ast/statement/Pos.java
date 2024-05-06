package fr.cyu.chromatynk.ast.statement;

import fr.cyu.chromatynk.ast.expr.Expr;

public class Pos implements Statement {

    private final Expr x;

    private final Expr y;

    public Pos(Expr x, Expr y) {
        this.x = x;
        this.y = y;
    }

    public Expr getX() {
        return x;
    }

    public Expr getY() {
        return y;
    }
}
