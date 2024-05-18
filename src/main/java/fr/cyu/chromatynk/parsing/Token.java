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
     * A lexical operator
     *
     * @param range the starting and ending position of this token
     * @param operator the symbol of this operator
     */
    record Operator(Range range, String operator) implements Token {}

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
     * A `FWD` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Fwd(Range range) implements Token {}

    /**
     * A `BWD` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Bwd(Range range) implements Token {}

    /**
     * A `TURN` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Turn(Range range) implements Token {}

    /**
     * A `MOV` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Mov(Range range) implements Token {}

    /**
     * A `POS` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Pos(Range range) implements Token {}

    /**
     * A `HIDE` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Hide(Range range) implements Token {}

    /**
     * A `SHOW` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Show(Range range) implements Token {}

    /**
     * A `PRESS` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Press(Range range) implements Token {}

    /**
     * A `COLOR` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Color(Range range) implements Token {}

    /**
     * A `THICK` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Thick(Range range) implements Token {}

    /**
     * A `LOOKAT` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record LookAt(Range range) implements Token {}

    /**
     * A `CURSOR` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Cursor(Range range) implements Token {}

    /**
     * A `SELECT` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Select(Range range) implements Token {}

    /**
     * A `REMOVE` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Remove(Range range) implements Token {}

    /**
     * A `IF` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record If(Range range) implements Token {}

    /**
     * A `ELSE` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Else(Range range) implements Token {}

    /**
     * A `FOR` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record For(Range range) implements Token {}

    /**
     * A `FROM` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record From(Range range) implements Token {}

    /**
     * A `TO` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record To(Range range) implements Token {}

    /**
     * A `STEP` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Step(Range range) implements Token {}

    /**
     * A `WHILE` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record While(Range range) implements Token {}

    /**
     * A `MIMIC` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Mimic(Range range) implements Token {}

    /**
     * A `MIRROR` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Mirror(Range range) implements Token {}

    /**
     * A `DEL` keyword.
     *
     * @param range the starting and ending position of this token
     */
    record Del(Range range) implements Token {}

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
            case Operator(Range ignored, String operator) -> operator;
            case ParenthesisOpen ignored -> "(";
            case ParenthesisClosed ignored -> ")";
            case BraceOpen ignored -> "{";
            case BraceClosed ignored -> "}";
            case Comma ignored -> ",";
            case Assign ignored -> "=";
            case Identifier(Range ignored, String name) -> name;
            case Token token -> token.getClass().getSimpleName().toUpperCase();
        };
    }
}
