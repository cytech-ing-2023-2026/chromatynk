package fr.cyu.chromatynk.ast.statement;

import fr.cyu.chromatynk.ast.expr.Expr;

public class Move implements Statement {

    private final Expr distanceX;

    private final Expr distanceY;

    public Move(Expr distanceX, Expr distanceY) {
        this.distanceX = distanceX;
        this.distanceY = distanceY;
    }

    public Expr getDistanceX() {
        return distanceX;
    }

    public Expr getDistanceY() {
        return distanceY;
    }
}
