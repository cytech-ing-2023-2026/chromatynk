package fr.cyu.chromatynk.ast;

import fr.cyu.chromatynk.eval.Value;
import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.Range;

import java.util.Optional;

/**
 * A Sealed interface Expr {}*
 * An interface named Expr with classes inside in order to use it
 */
public sealed interface Expr {

    /**
     * Get the range of this expression.
     *
     * @return the starting and ending position of this expression
     */
    Range range();

    /**
     * A literal boolean.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param value the literal value
     */
    record LiteralBool(Range range, boolean value) implements Expr {}

    /**
     * A literal String.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param value the literal value
     */
    record LiteralString(Range range, String value) implements Expr {}

    /**
     * A literal integer.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param value the literal value
     */
    record LiteralInt(Range range, int value) implements Expr {}

    /**
     * A literal float.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param value the literal value
     */
    record LiteralFloat(Range range, double value) implements Expr {}

    /**
     * A RGBA literal color.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param red     the red color
     * @param green   the green color
     * @param blue    the blue color
     * @param opacity the opacity of the color chosen
     */
    record LiteralColor(Range range, double red, double green, double blue, double opacity) implements Expr {}

    /**
     * A percentage.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param expr the value of the percent
     */
    record Percent(Range range, Expr expr) implements Expr {}

    /**
     * A value negation.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param expr the expression to negate.
     */
    record Negation(Range range, Expr expr) implements Expr {}

    /**
     * An addition {@code left+right}.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the addition
     * @param right the right member of the addition
     */
    record Add(Range range, Expr left, Expr right) implements Expr {}

    /**
     * A subtraction {@code left-right}.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the subtraction
     * @param right the right member of the subtraction
     */
    record Sub(Range range, Expr left, Expr right) implements Expr {}

    /**
     * A multiplication {@code left*right}.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the multiplication
     * @param right the right member of the multiplication
     */
    record Mul(Range range, Expr left, Expr right) implements Expr {}

    /**
     * A division {@code left+right}.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the division
     * @param right the right member of the division
     */
    record Div(Range range, Expr left, Expr right) implements Expr {}


    /**
     * A boolean NOT.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param expr the inverted expression
     */
    record Not(Range range, Expr expr) implements Expr {}

    /**
     * A boolean OR.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the OR
     * @param right the right member of the OR
     */
    record Or(Range range, Expr left, Expr right) implements Expr {}

    /**
     * A boolean AND.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the AND
     * @param right the right member of the AND
     */
    record And(Range range, Expr left, Expr right) implements Expr {}

    /**
     * A comparison `left == right`.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the comparison
     * @param right the right member of the comparison
     */
    record Equal(Range range, Expr left, Expr right) implements Expr {}

    /**
     * A comparison `left != right`.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the comparison
     * @param right the right member of the comparison
     */
    record NotEqual(Range range, Expr left, Expr right) implements Expr {}

    /**
     * A comparison `left > right`.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the comparison
     * @param right the right member of the comparison
     */
    record Greater(Range range, Expr left, Expr right) implements Expr {}

    /**
     * A comparison `left < right`.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the comparison
     * @param right the right member of the comparison
     */
    record Less(Range range, Expr left, Expr right) implements Expr {}

    /**
     * A comparison `left >= right`.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the comparison
     * @param right the right member of the comparison
     */
    record GreaterEqual(Range range, Expr left, Expr right) implements Expr {}

    /**
     * A comparison `left <= right`.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param left  the left member of the comparison
     * @param right the right member of the comparison
     */
    record LessEqual(Range range, Expr left, Expr right) implements Expr {}

    /**
     * Get the value of a variable.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param name the name of variable
     */
    record VarCall(Range range, String name) implements Expr {}

    /**
     * Set the color of the current cursor.
     *
     * @param range the starting and ending {@link Position} of this expression
     * @param color the new color the cursor will use while drawing
     */
    record Color(Range range, Expr color) implements Expr {}
}

