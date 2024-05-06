package fr.cyu.chromatynk.ast.statement;

import fr.cyu.chromatynk.ast.expr.Expr;

public class While implements Statement {

    private final Expr condition;

    private final Statement body;

    public While(Expr condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    public Expr getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }
}
