package fr.cyu.chromatynk.ast.expr;

public class LiteralString {

    private final String value;

    public LiteralString(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}

