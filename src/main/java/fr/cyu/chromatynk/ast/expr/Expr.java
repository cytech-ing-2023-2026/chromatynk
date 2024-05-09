package fr.cyu.chromatynk.ast.expr;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.eval.Value;

import java.util.Optional;

/**
 * A Sealed interface Expr {}*
 * An interface named Expr with classes inside in order to use it
 */
public sealed interface Expr {

    /**
     * A literal boolean.
     *
     * @param value the literal value
     */
    record LiteralBool(boolean value) implements Expr {}

    /**
     * A literal String.
     *
     * @param value the literal value
     */
    record LiteralString(String value) implements Expr {}

    /**
     * A literal integer.
     *
     * @param value the literal value
     */
    record LiteralInt(int value) implements Expr {}

    /**
     * A literal float.
     *
     * @param value the literal value
     */
    record LiteralFloat(double value) implements Expr {}

    /**
     * A RGBA literal color.
     *
     * @param red     the red color
     * @param green   the green color
     * @param blue    the blue color
     * @param opacity the opacity of the color chosen
     */
    record LiteralColor(byte red, byte green, byte blue, byte opacity) implements Expr {}

    /**
     * A percentage.
     *
     * @param expr the value of the percent
     */
    record Percent(Expr expr) implements Expr {}

    /**
     * A value negation.
     *
     * @param expr the expression to negate.
     */
    record Negation(Expr expr) implements Expr {}

    /**
     * An addition {@code left+right}.
     *
     * @param left  the left member of the addition
     * @param right the right member of the addition
     */
    record Add(Expr left, Expr right) implements Expr {}

    /**
     * A subtraction {@code left-right}.
     *
     * @param left  the left member of the subtraction
     * @param right the right member of the subtraction
     */
    record Sub(Expr left, Expr right) implements Expr {}

    /**
     * A multiplication {@code left*right}.
     *
     * @param left  the left member of the multiplication
     * @param right the right member of the multiplication
     */
    record Mul(Expr left, Expr right) implements Expr {}

    /**
     * A division {@code left+right}.
     *
     * @param left  the left member of the division
     * @param right the right member of the division
     */
    record Div(Expr left, Expr right) implements Expr {}


    /**
     * A boolean NOT.
     *
     * @param expr the inverted expression
     */
    record Not(Expr expr) implements Expr {}

    /**
     * A boolean OR.
     *
     * @param left  the left member of the OR
     * @param right the right member of the OR
     */
    record Or(Expr left, Expr right) implements Expr {}

    /**
     * A boolean AND.
     *
     * @param left  the left member of the AND
     * @param right the right member of the AND
     */
    record And(Expr left, Expr right) implements Expr {}

    /**
     * Get the value of a variable.
     *
     * @param name the name of variable
     */
    record VarCall(String name) implements Expr {}

    /**
     * Set the color of the current cursor.
     *
     * @param color the new color the cursor will use while drawing
     */
    record Color(Expr color) implements Expr {}

    /**
     * A variable declaration.
     *
     * @param type the type of the variable
     * @param name the name of the variable
     * @param value the optional value assigned to the variable when declared
     */
    record VarDeclaration(Type type, String name, Optional<Value> value) implements Expr {}

    /**
     * Delete a variable.
     *
     * @param name the name of the variable to delete
     */
    record Del(String name) implements Expr {}
}

