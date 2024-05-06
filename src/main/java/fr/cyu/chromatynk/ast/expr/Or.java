package fr.cyu.chromatynk.ast.expr;

public class Or implements Expr{
    private final Expr left;
    private final Expr right;

    public Or(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }
}
