package fr.cyu.chromatynk.ast.statement;

import fr.cyu.chromatynk.ast.expr.Expr;

public class Turn implements Statement {

    private final Expr angle;

    public Turn(Expr angle) {
        this.angle = angle;
    }

    public Expr getAngle() {
        return angle;
    }
}
