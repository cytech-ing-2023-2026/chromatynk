package fr.cyu.chromatynk.ast.statement;

import fr.cyu.chromatynk.ast.expr.Expr;

public class Backward implements Statement {

    private final Expr distance;

    public Backward(Expr distance) {
        this.distance = distance;
    }

    public Expr getDistance() {
        return distance;
    }
}
