package fr.cyu.chromatynk.ast.expr;

public class VarCall implements Expr {
    private final String name;

    public VarCall(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

