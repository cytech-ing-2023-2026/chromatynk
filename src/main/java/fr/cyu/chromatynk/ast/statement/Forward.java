package fr.cyu.chromatynk.ast.statement;

import fr.cyu.chromatynk.ast.expr.Expr;

public class Forward implements Statement {

    private final Expr distance;

    public Forward(Expr distance) {
        this.distance = distance;
    }

    public Expr getDistance() {
        return distance;
    }
}
