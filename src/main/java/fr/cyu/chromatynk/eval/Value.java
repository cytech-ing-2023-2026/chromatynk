package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;

public sealed interface Value {

    record Bool(boolean value) implements Value {}
    record Int(int value) implements Value {}
    record Float(float value) implements Value {}
    record Str(String value) implements Value {}
    record Color(byte red, byte green, byte blue, byte alpha) implements Value {}

    default Type getType() {
        return switch (this) {
            case Bool x  -> Type.BOOLEAN;
            case Int x   -> Type.INT;
            case Float x -> Type.FLOAT;
            case Str x   -> Type.STRING;
            case Color x -> Type.COLOR;
        };
    }
}
