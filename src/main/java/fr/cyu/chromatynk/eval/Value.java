package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;

/**
 * The result of the evaluation of an expression.
 */
public sealed interface Value {

    /**
     * A boolean.
     *
     * @param value the value of this boolean
     */
    record Bool(boolean value) implements Value {}

    /**
     * An integer.
     *
     * @param value the value of this integer
     */
    record Int(int value) implements Value {}

    /**
     * A floating number (double precision).
     *
     * @param value the value of this floating number
     */
    record Float(double value) implements Value {}

    /**
     * A percentage.
     *
     * @param value the value of this percentage
     */
    record Percentage(double value) implements Value {}

    /**
     * A string.
     *
     * @param value the value of this string
     */
    record Str(String value) implements Value {}

    /**
     * A RGBA color.
     *
     * @param red the red component between 0 and 1
     * @param green the green component between 0 and 1
     * @param blue the blue component between 0 and 1
     * @param alpha the opacity between 0 and 1
     */
    record Color(double red, double green, double blue, double alpha) implements Value {}

    /**
     * Get the type of this value.
     */
    default Type getType() {
        return switch (this) {
            case Bool x -> Type.BOOLEAN;
            case Int x -> Type.INT;
            case Float x -> Type.FLOAT;
            case Percentage x -> Type.PERCENTAGE;
            case Str x -> Type.STRING;
            case Color x -> Type.COLOR;
        };
    }
}
