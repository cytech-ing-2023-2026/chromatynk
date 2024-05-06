package fr.cyu.chromatynk.ast.expr;

public class LiteralColor implements Expr {

    private final byte red;
    private final byte green;
    private final byte blue;

    public LiteralColor(byte red, byte green, byte blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public byte getRed() {
        return red;
    }

    public byte getGreen() {
        return green;
    }

    public byte getBlue() {
        return blue;
    }
}
