package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.PrettyPrintable;
import fr.cyu.chromatynk.util.Range;

/**
 * A lexical token. For example {@code FWD 5%} is parsed to {@code Fwd() LiteralInt(5) Percent()},
 * each token containing its starting and ending positions.
 */
public sealed interface Token extends PrettyPrintable {

    /**
     * The starting and ending position of this {@link Token}.
     */
    Range range();

    /**
     * A boolean literal.
     *
     * @param range the starting and ending position of this token
     * @param value the value of this literal
     */
    record LiteralBool(Range range, boolean value) implements Token {}

    /**
     * A string literal.
     *
     * @param range the starting and ending position of this token
     * @param value the value of this literal
     */
    record LiteralString(Range range, String value) implements Token {}

    /**
     * A integer literal.
     *
     * @param range the starting and ending position of this token
     * @param value the value of this literal
     */
    record LiteralInt(Range range, int value) implements Token {}

    /**
     * A float literal.
     *
     * @param range the starting and ending position of this token
     * @param value the value of this literal
     */
    record LiteralFloat(Range range, double value) implements Token {}

    /**
     * A color literal.
     *
     * @param range the starting and ending position of this token
     * @param hex the value of this literal in RGB or RGBA hexadecimal
     */
    record LiteralColor(Range range, String hex) implements Token {}

    /**
     * An open parenthesis `(` symbol.
     *
     * @param range the starting and ending position of this token
     */
    record ParenthesisOpen(Range range) implements Token {}

    /**
     * A closed parenthesis `)` symbol.
     *
     * @param range the starting and ending position of this token
     */
    record ParenthesisClosed(Range range) implements Token {}

    /**
     * An open brace `{` symbol.
     *
     * @param range the starting and ending position of this token
     */
    record BraceOpen(Range range) implements Token {}

    /**
     * An closed brace `}` symbol.
     *
     * @param range the starting and ending position of this token
     */
    record BraceClosed(Range range) implements Token {}

    /**
     * An arrow which delimits a one line body
     *
     * @param range the starting and ending position of this token
     */
    record Arrow(Range range) implements Token{}

    /**
     * A comma.
     *
     * @param range the starting and ending position of this token
     */
    record Comma(Range range) implements Token {}

    /**
     * An equal `=` symbol.
     *
     * @param range the starting and ending position of this token
     */
    record Assign(Range range) implements Token {}

    /**
     * A lexical operator
     */
    sealed interface Operator extends Token {}

    /**
     * {@code &&} operator.
     *
     * @param range the starting and ending position of this token
     */
    record And(Range range) implements Operator {}

    /**
     * {@code ||} operator.
     *
     * @param range the starting and ending position of this token
     */
    record Or(Range range) implements Operator {}

    /**
     * {@code ==} operator.
     *
     * @param range the starting and ending position of this token
     */
    record Equal(Range range) implements Operator {}

    /**
     * {@code !=} operator.
     *
     * @param range the starting and ending position of this token
     */
    record NotEqual(Range range) implements Operator {}

    /**
     * {@code !} operator.
     *
     * @param range the starting and ending position of this token
     */
    record Not(Range range) implements Operator {}

    /**
     * {@code >} operator.
     *
     * @param range the starting and ending position of this token
     */
    record Greater(Range range) implements Operator {}

    /**
     * {@code <} operator.
     *
     * @param range the starting and ending position of this token
     */
    record Less(Range range) implements Operator {}

    /**
     * {@code >=} operator.
     *
     * @param range the starting and ending position of this token
     */
    record GreaterEqual(Range range) implements Operator {}

    /**
     * {@code <=} operator.
     *
     * @param range the starting and ending position of this token
     */
    record LessEqual(Range range) implements Operator {}

    /**
     * {@code %} operator.
     *
     * @param range the starting and ending position of this token
     */
    record Percent(Range range) implements Operator {}

    /**
     * {@code +} operator.
     *
     * @param range the starting and ending position of this token
     */
    record Plus(Range range) implements Operator {}

    /**
     * {@code -} operator.
     *
     * @param range the starting and ending position of this token
     */
    record Minus(Range range) implements Operator {}

    /**
     * {@code *} operator.
     *
     * @param range the starting and ending position of this token
     */
    record Mul(Range range) implements Operator {}

    /**
     * {@code /} operator.
     *
     * @param range the starting and ending position of this token
     */
    record Div(Range range) implements Operator {}

    /**
     * A keyword token.
     */
    sealed interface Keyword extends Token {}

    /**
     * A `FWD` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Fwd(Range range) implements Keyword {}

    /**
     * A `BWD` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Bwd(Range range) implements Keyword {}

    /**
     * A `TURN` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Turn(Range range) implements Keyword {}

    /**
     * A `MOV` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Mov(Range range) implements Keyword {}

    /**
     * A `POS` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Pos(Range range) implements Keyword {}

    /**
     * A `HIDE` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Hide(Range range) implements Keyword {}

    /**
     * A `SHOW` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Show(Range range) implements Keyword {}

    /**
     * A `PRESS` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Press(Range range) implements Keyword {}

    /**
     * A `COLOR` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Color(Range range) implements Keyword {}

    /**
     * A `THICK` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Thick(Range range) implements Keyword {}

    /**
     * A `LOOKAT` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record LookAt(Range range) implements Keyword {}

    /**
     * A `CURSOR` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Cursor(Range range) implements Keyword {}

    /**
     * A `SELECT` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Select(Range range) implements Keyword {}

    /**
     * A `REMOVE` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Remove(Range range) implements Keyword {}

    /**
     * A `IF` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record If(Range range) implements Keyword {}

    /**
     * A `ELSE` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Else(Range range) implements Keyword {}

    /**
     * A `FOR` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record For(Range range) implements Keyword {}

    /**
     * A `FROM` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record From(Range range) implements Keyword {}

    /**
     * A `TO` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record To(Range range) implements Keyword {}

    /**
     * A `STEP` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Step(Range range) implements Keyword {}

    /**
     * A `WHILE` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record While(Range range) implements Keyword {}

    /**
     * A `MIMIC` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Mimic(Range range) implements Keyword {}

    /**
     * A `MIRROR` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Mirror(Range range) implements Keyword {}

    /**
     * A `DEL` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Del(Range range) implements Keyword {}

    /**
     * An identifier for a variable or a function.
     *
     * @param range the starting and ending position of this token
     * @param name the name of this identifier
     */
    record Identifier(Range range, String name) implements Token {}

    /**
     * End of file.
     *
     * @param position the position of the end of the file
     */
    record EndOfFile(Position position) implements Token {

        @Override
        public Range range() {
            return new Range(position, position);
        }
    }

    @Override
    default String toPrettyString() {
        return switch (this) {
            case LiteralBool(Range ignored, boolean value) -> String.valueOf(value);
            case LiteralString(Range ignored, String value) -> String.valueOf(value);
            case LiteralInt(Range ignored, int value) -> String.valueOf(value);
            case LiteralFloat(Range ignored, double value) -> String.valueOf(value);
            case LiteralColor(Range ignored, String hex) -> hex;
            case ParenthesisOpen ignored -> "(";
            case ParenthesisClosed ignored -> ")";
            case BraceOpen ignored -> "{";
            case BraceClosed ignored -> "}";
            case Comma ignored -> ",";
            case Assign ignored -> "=";
            case And ignored -> "&&";
            case Or ignored -> "||";
            case Equal ignored -> "==";
            case NotEqual ignored -> "!=";
            case Not ignored -> "!";
            case Greater ignored -> ">";
            case Less ignored -> "<";
            case GreaterEqual ignored -> ">=";
            case LessEqual ignored -> "<=";
            case Plus ignored -> "+";
            case Minus ignored -> "-";
            case Mul ignored -> "*";
            case Div ignored -> "/";
            case Identifier(Range ignored, String name) -> name;
            case Token token -> token.getClass().getSimpleName().toUpperCase();
        };
    }
}
