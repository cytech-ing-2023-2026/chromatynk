package fr.cyu.chromatynk.test.parsing;

import fr.cyu.chromatynk.parsing.Lexer;
import fr.cyu.chromatynk.parsing.ParsingException;
import fr.cyu.chromatynk.parsing.ParsingIterator;
import fr.cyu.chromatynk.parsing.UnexpectedInputException;
import fr.cyu.chromatynk.util.Range;
import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.cyu.chromatynk.parsing.Token.*;
import static fr.cyu.chromatynk.test.parsing.ParserTest.assertParseFailure;
import static fr.cyu.chromatynk.test.parsing.ParserTest.assertParseString;

public class LexerTestCase {

    @Test
    public void literalBool() {
        assertParseString(new LiteralBool(Range.sameLine(0, 4), true), Lexer.LITERAL_BOOL_PARSER, "true");
        assertParseString(new LiteralBool(Range.sameLine(0, 5), false), Lexer.LITERAL_BOOL_PARSER, "false");
        assertParseFailure(UnexpectedInputException.class, Lexer.LITERAL_BOOL_PARSER, ParsingIterator.fromString("idk"));
        assertParseFailure(UnexpectedInputException.class, Lexer.LITERAL_BOOL_PARSER, ParsingIterator.fromString("falsee"));
    }

    @Test
    public void literalString() {
        assertParseString(new LiteralString(Range.sameLine(0, 5), "abc"), Lexer.LITERAL_STRING_PARSER, "\"abc\"");
        assertParseString(new LiteralString(Range.sameLine(0, 13), "Hello World"), Lexer.LITERAL_STRING_PARSER, "\"Hello World\"");
        assertParseFailure(UnexpectedInputException.class, Lexer.LITERAL_STRING_PARSER, ParsingIterator.fromString("abc"));
        assertParseFailure(UnexpectedInputException.class, Lexer.LITERAL_STRING_PARSER, ParsingIterator.fromString("\"abc"));
        assertParseFailure(UnexpectedInputException.class, Lexer.LITERAL_STRING_PARSER, ParsingIterator.fromString("abc\""));
    }

    @Test
    public void literalInt() {
        assertParseString(new LiteralInt(Range.sameLine(0, 1), 1), Lexer.LITERAL_INT_PARSER, "1");
        assertParseString(new LiteralInt(Range.sameLine(0, 2), 42), Lexer.LITERAL_INT_PARSER, "42");
        assertParseFailure(UnexpectedInputException.class, Lexer.LITERAL_INT_PARSER, ParsingIterator.fromString("abc"));
    }

    @Test
    public void literalFloat() {
        assertParseString(new LiteralFloat(Range.sameLine(0, 1), 1), Lexer.LITERAL_FLOAT_PARSER, "1");
        assertParseString(new LiteralFloat(Range.sameLine(0, 2), 42), Lexer.LITERAL_FLOAT_PARSER, "42");
        assertParseString(new LiteralFloat(Range.sameLine(0, 4), 42.5), Lexer.LITERAL_FLOAT_PARSER, "42.5");
        assertParseString(new LiteralFloat(Range.sameLine(0, 2), 42), Lexer.LITERAL_FLOAT_PARSER, "42.");
        assertParseFailure(UnexpectedInputException.class, Lexer.LITERAL_FLOAT_PARSER, ParsingIterator.fromString(".5"));
        assertParseFailure(UnexpectedInputException.class, Lexer.LITERAL_FLOAT_PARSER, ParsingIterator.fromString("abc"));
    }

    @Test
    public void literalColor() {
        assertParseString(new LiteralColor(Range.sameLine(0, 4), "#ABC"), Lexer.LITERAL_COLOR_PARSER, "#ABC");
        assertParseString(new LiteralColor(Range.sameLine(0, 4), "#123"), Lexer.LITERAL_COLOR_PARSER, "#123");
        assertParseString(new LiteralColor(Range.sameLine(0, 5), "#ABCD"), Lexer.LITERAL_COLOR_PARSER, "#ABCD");
        assertParseString(new LiteralColor(Range.sameLine(0, 7), "#AABBCC"), Lexer.LITERAL_COLOR_PARSER, "#AABBCC");
        assertParseString(new LiteralColor(Range.sameLine(0, 9), "#AABBCCDD"), Lexer.LITERAL_COLOR_PARSER, "#AABBCCDD");
        assertParseString(new LiteralColor(Range.sameLine(0, 5), "#ABCD"), Lexer.LITERAL_COLOR_PARSER, "#ABCDE");
        assertParseString(new LiteralColor(Range.sameLine(0, 9), "#AABBCCDD"), Lexer.LITERAL_COLOR_PARSER, "#AABBCCDDEE");
        assertParseFailure(UnexpectedInputException.class, Lexer.LITERAL_COLOR_PARSER, ParsingIterator.fromString("#GGG"));
    }

    @Test
    public void operators() {
        assertParseString(new Operator(Range.sameLine(0, 1), "%"), Lexer.OPERATOR_PARSER, "%");
        assertParseString(new Operator(Range.sameLine(0, 1), "+"), Lexer.OPERATOR_PARSER, "+");
        assertParseString(new Operator(Range.sameLine(0, 1), "-"), Lexer.OPERATOR_PARSER, "-");
        assertParseString(new Operator(Range.sameLine(0, 1), "*"), Lexer.OPERATOR_PARSER, "*");
        assertParseString(new Operator(Range.sameLine(0, 1), "/"), Lexer.OPERATOR_PARSER, "/");
        assertParseString(new Operator(Range.sameLine(0, 1), "!"), Lexer.OPERATOR_PARSER, "!");
        assertParseString(new Operator(Range.sameLine(0, 2), "&&"), Lexer.OPERATOR_PARSER, "&&");
        assertParseString(new Operator(Range.sameLine(0, 2), "||"), Lexer.OPERATOR_PARSER, "||");
        assertParseString(new Operator(Range.sameLine(0, 2), "=="), Lexer.OPERATOR_PARSER, "==");
        assertParseString(new Operator(Range.sameLine(0, 2), "!="), Lexer.OPERATOR_PARSER, "!=");
        assertParseString(new Operator(Range.sameLine(0, 2), ">="), Lexer.OPERATOR_PARSER, ">=");
        assertParseString(new Operator(Range.sameLine(0, 2), "<="), Lexer.OPERATOR_PARSER, "<=");
        assertParseString(new Operator(Range.sameLine(0, 1), ">"), Lexer.OPERATOR_PARSER, ">");
        assertParseString(new Operator(Range.sameLine(0, 1), "<"), Lexer.OPERATOR_PARSER, "<");
        assertParseFailure(ParsingException.class, Lexer.OPERATOR_PARSER, ParsingIterator.fromString("?"));
    }

    @Test
    public void symbols() {
        assertParseString(new ParenthesisOpen(Range.sameLine(0, 1)), Lexer.SYMBOL_PARSER, "(");
        assertParseString(new ParenthesisClosed(Range.sameLine(0, 1)), Lexer.SYMBOL_PARSER, ")");
        assertParseString(new BraceOpen(Range.sameLine(0, 1)), Lexer.SYMBOL_PARSER, "{");
        assertParseString(new BraceClosed(Range.sameLine(0, 1)), Lexer.SYMBOL_PARSER, "}");
        assertParseString(new Assign(Range.sameLine(0, 1)), Lexer.SYMBOL_PARSER, "=");
        assertParseFailure(ParsingException.class, Lexer.SYMBOL_PARSER, ParsingIterator.fromString(""));
        assertParseFailure(ParsingException.class, Lexer.SYMBOL_PARSER, ParsingIterator.fromString("$"));
    }

    @Test
    public void keywords() {
        assertParseString(new Fwd(Range.sameLine(0, 3)), Lexer.KEYWORD_PARSER, "FWD");
        assertParseString(new Bwd(Range.sameLine(0, 3)), Lexer.KEYWORD_PARSER, "BWD");
        assertParseString(new Turn(Range.sameLine(0, 4)), Lexer.KEYWORD_PARSER, "TURN");
        assertParseString(new Mov(Range.sameLine(0, 3)), Lexer.KEYWORD_PARSER, "MOV");
        assertParseString(new Pos(Range.sameLine(0, 3)), Lexer.KEYWORD_PARSER, "POS");
        assertParseString(new Hide(Range.sameLine(0, 4)), Lexer.KEYWORD_PARSER, "HIDE");
        assertParseString(new Show(Range.sameLine(0, 4)), Lexer.KEYWORD_PARSER, "SHOW");
        assertParseString(new Press(Range.sameLine(0, 5)), Lexer.KEYWORD_PARSER, "PRESS");
        assertParseString(new Color(Range.sameLine(0, 5)), Lexer.KEYWORD_PARSER, "COLOR");
        assertParseString(new Thick(Range.sameLine(0, 5)), Lexer.KEYWORD_PARSER, "THICK");
        assertParseString(new LookAt(Range.sameLine(0, 6)), Lexer.KEYWORD_PARSER, "LOOKAT");
        assertParseString(new Cursor(Range.sameLine(0, 6)), Lexer.KEYWORD_PARSER, "CURSOR");
        assertParseString(new Select(Range.sameLine(0, 6)), Lexer.KEYWORD_PARSER, "SELECT");
        assertParseString(new Remove(Range.sameLine(0, 6)), Lexer.KEYWORD_PARSER, "REMOVE");
        assertParseString(new If(Range.sameLine(0, 2)), Lexer.KEYWORD_PARSER, "IF");
        assertParseString(new Else(Range.sameLine(0, 4)), Lexer.KEYWORD_PARSER, "ELSE");
        assertParseString(new For(Range.sameLine(0, 3)), Lexer.KEYWORD_PARSER, "FOR");
        assertParseString(new To(Range.sameLine(0, 2)), Lexer.KEYWORD_PARSER, "TO");
        assertParseString(new Step(Range.sameLine(0, 4)), Lexer.KEYWORD_PARSER, "STEP");
        assertParseString(new While(Range.sameLine(0, 5)), Lexer.KEYWORD_PARSER, "WHILE");
        assertParseString(new Mimic(Range.sameLine(0, 5)), Lexer.KEYWORD_PARSER, "MIMIC");
        assertParseString(new Mirror(Range.sameLine(0, 6)), Lexer.KEYWORD_PARSER, "MIRROR");
        assertParseString(new Num(Range.sameLine(0, 3)), Lexer.KEYWORD_PARSER, "NUM");
        assertParseString(new Str(Range.sameLine(0, 3)), Lexer.KEYWORD_PARSER, "STR");
        assertParseString(new Bool(Range.sameLine(0, 4)), Lexer.KEYWORD_PARSER, "BOOL");
        assertParseString(new Del(Range.sameLine(0, 3)), Lexer.KEYWORD_PARSER, "DEL");
        assertParseFailure(ParsingException.class, Lexer.KEYWORD_PARSER, ParsingIterator.fromString(""));
        assertParseFailure(ParsingException.class, Lexer.KEYWORD_PARSER, ParsingIterator.fromString("VAR"));
    }

    @Test
    public void identifier() {
        assertParseString(new Identifier(Range.sameLine(0, 3), "abc"), Lexer.IDENTIFIER_PARSER, "abc");
        assertParseString(new Identifier(Range.sameLine(0, 3), "a23"), Lexer.IDENTIFIER_PARSER, "a23");
        assertParseString(new Identifier(Range.sameLine(0, 7), "abc_def"), Lexer.IDENTIFIER_PARSER, "abc_def");
        assertParseString(new Identifier(Range.sameLine(0, 4), "_abc"), Lexer.IDENTIFIER_PARSER, "_abc");
        assertParseFailure(ParsingException.class, Lexer.IDENTIFIER_PARSER, ParsingIterator.fromString(""));
        assertParseFailure(ParsingException.class, Lexer.IDENTIFIER_PARSER, ParsingIterator.fromString("123"));
        assertParseFailure(ParsingException.class, Lexer.IDENTIFIER_PARSER, ParsingIterator.fromString("1bc"));
    }

    @Test
    public void tokens() {
        assertParseString(List.of(new LiteralInt(Range.sameLine(0, 1), 5)), Lexer.TOKENS_PARSER, "5");

        assertParseString(
                List.of(new LiteralInt(Range.sameLine(0, 1), 5), new Operator(Range.sameLine(1, 2), "%")),
                Lexer.TOKENS_PARSER,
                "5%"
        );

        assertParseString(
                List.of(new LiteralInt(Range.sameLine(0, 1), 5), new Operator(Range.sameLine(2, 3), "%")),
                Lexer.TOKENS_PARSER,
                "5 %"
        );

        assertParseString(
                List.of(new Fwd(Range.sameLine(0, 3)), new LiteralInt(Range.sameLine(4, 5), 5), new Operator(Range.sameLine(5, 6), "%")),
                Lexer.TOKENS_PARSER,
                "FWD 5%"
        );

        assertParseString(
                List.of(
                        new For(Range.sameLine(0, 3)),
                        new Identifier(Range.sameLine(4, 5), "i"),
                        new From(Range.sameLine(6, 10)),
                        new LiteralInt(Range.sameLine(11, 12), 0),
                        new To(Range.sameLine(13, 15)),
                        new LiteralInt(Range.sameLine(16, 17), 5),
                        new BraceOpen(Range.sameLine(18, 19)),
                        new Fwd(Range.sameLine(2, 5, 1)),
                        new Identifier(Range.sameLine(6, 7, 1), "i"),
                        new Operator(Range.sameLine(7, 8, 1), "%"),
                        new BraceClosed(Range.sameLine(0, 1, 2))
                        ),
                Lexer.TOKENS_PARSER,
                """
                        FOR i FROM 0 TO 5 {
                          FWD i%
                        }
                        """
        );
    }
}
