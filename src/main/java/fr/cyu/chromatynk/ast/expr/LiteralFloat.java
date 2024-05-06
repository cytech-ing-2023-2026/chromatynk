package fr.cyu.chromatynk.ast.expr;

public class LiteralFloat {

    private final double value;

    public LiteralFloat(double value) {
        this.value = value;
    }

    public double getValue(){
        return value;
    }
}

