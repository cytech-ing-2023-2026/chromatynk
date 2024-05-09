package fr.cyu.chromatynk.ast.expr;

/** A Sealed interface Expr {}*
 *An interface named Expr with classes inside
 */
public sealed interface Expr {
    /** An int {@code value}*
     * @param value the int value wanted
     */
    record LiteralInt(int value) implements Expr{}
    /** A String {@code value}*
     * @param value the string value wanted
     */
    record LiteralString(String value) implements Expr{}
    /** A Bool {@code value}*
     * @param value the boolean value wanted
     */
    record LiteralBool(boolean value) implements Expr{}

    /** An addition {@code left+right}*
     * @param left the left member of the addition
     * @param right the right member of the addition
     */
    record Add(Expr left, Expr right) implements Expr{}
    /** A Substraction {@code left+right}*
     * @param left the left member of the substraction
     * @param right the right member of the substraction
     */
    record Sub(Expr left, Expr right) implements Expr{}
    /** A Substraction {@code left+right}*
     * @param left the left member of the multiplication
     * @param right the right member of the multiplication
     */
    record Mul(Expr left, Expr right) implements Expr{}
    /** A multiplication {@code left+right}*
     * @param left the left member of the division
     * @param right the right member of the division
     */
    record Div(Expr left, Expr right) implements Expr{}
    /** A combination {@code left+right}*
     * @param left the left member of the combination
     * @param right the right member of the combination
     */
    record And(Expr left, Expr right) implements Expr{}
    /** A color {@code red, green, blue, opacity}*
     * @param red the red color
     * @param green the green color
     * @param blue the blue color
     * @param opacity the opacity of the color color
     */
    record LiteralColor(byte red, byte green, byte blue, byte opacity) implements Expr{}
    /** A Float {@code value}*
     * @param value the float value
     */
    record LiteralFloat(double value) implements Expr{}
    /** A Negation {@code value}*
     * @param value the negation value
     */
    record Negation(Expr value) implements Expr{}
    /** An other type of a negation = Not {@code value}*
     * @param value the "negation" value in order to show a difference
     */
    record Not(Expr value) implements Expr{}
    /** A Or {@code right and left}*
     * @param left the left member of the choice
     * @param right the right member of the choice
     */
    record Or(Expr left, Expr right) implements Expr{}
    /** A Percent {@code value}*
     * @param value the percent value
     */
    record Percent(Expr value) implements Expr{}
    /** A call for a value {@code name}*
     * @param name the name of the value called
     */
    record VarCall(String name) implements Expr{}
    /** A Color {@code webColor}*
     * @param webColor the color wanted
     */
    record Color(byte webColor) implements Expr{}
    /** A Numeric value {@code name}*
     * @param name the name of the numeric value
     */
    record Num(Expr name) implements Expr{}
    /** A String value {@code name}*
     * @param name the name of the string value
     */
    record Str(String name) implements Expr{}
    /** A Boolean {@code value}*
     * @param value the boolean value
     */
    record Bool(boolean value) implements Expr{}
    /** A Delete class {@code name}*
     * @param name the name of the deleted value
     */
    record Del(String name) implements Expr{}
}

