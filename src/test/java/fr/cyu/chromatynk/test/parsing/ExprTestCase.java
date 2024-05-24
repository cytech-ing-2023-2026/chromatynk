package fr.cyu.chromatynk.test.parsing;

import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.parsing.ExprParser;
import fr.cyu.chromatynk.parsing.ParsingIterator;
import fr.cyu.chromatynk.parsing.Token;
import fr.cyu.chromatynk.parsing.UnexpectedInputException;
import fr.cyu.chromatynk.util.Range;
import org.junit.jupiter.api.Test;

import static fr.cyu.chromatynk.test.parsing.ParserTest.assertParse;
import static fr.cyu.chromatynk.test.parsing.ParserTest.assertParseFailure;

public class ExprTestCase {

    @Test
    public void literal() {
        assertParse(
                new Expr.LiteralBool(Range.sameLine(0, 4), true),
                ExprParser.literal(),
                ParsingIterator.of(new Token.LiteralBool(Range.sameLine(0, 4), true))
        );

        assertParse(
                new Expr.LiteralString(Range.sameLine(0, 5), "abc"),
                ExprParser.literal(),
                ParsingIterator.of(new Token.LiteralString(Range.sameLine(0, 5), "abc"))
        );

        assertParse(
                new Expr.LiteralInt(Range.sameLine(0, 1), 5),
                ExprParser.literal(),
                ParsingIterator.of(new Token.LiteralInt(Range.sameLine(0, 1), 5))
        );

        assertParse(
                new Expr.LiteralFloat(Range.sameLine(0, 3), 2.5),
                ExprParser.literal(),
                ParsingIterator.of(new Token.LiteralFloat(Range.sameLine(0, 3), 2.5))
        );

        assertParse(
                new Expr.LiteralColor(Range.sameLine(0, 5), 1, 0, 0, 1),
                ExprParser.literal(),
                ParsingIterator.of(new Token.LiteralColor(Range.sameLine(0, 5), "#F00F"))
        );

        assertParse(
                new Expr.LiteralColor(Range.sameLine(0, 5), 1, 0, 0, 1),
                ExprParser.literal(),
                ParsingIterator.of(new Token.LiteralColor(Range.sameLine(0, 5), "#FF0000"))
        );
    }

    @Test
    public void prefixOperator() {
        assertParse(
                new Expr.LiteralInt(Range.sameLine(1, 2), 5),
                ExprParser.prefixOperator(),
                ParsingIterator.of(new Token.Plus(Range.sameLine(0, 1)), new Token.LiteralInt(Range.sameLine(1, 2), 5))
        );

        assertParse(
                new Expr.Negation(Range.sameLine(0, 2), new Expr.LiteralInt(Range.sameLine(1, 2), 5)),
                ExprParser.prefixOperator(),
                ParsingIterator.of(new Token.Minus(Range.sameLine(0, 1)), new Token.LiteralInt(Range.sameLine(1, 2), 5))
        );

        assertParse(
                new Expr.Not(Range.sameLine(0, 5), new Expr.LiteralBool(Range.sameLine(1, 5), true)),
                ExprParser.prefixOperator(),
                ParsingIterator.of(new Token.Not(Range.sameLine(0, 1)), new Token.LiteralBool(Range.sameLine(1, 5), true))
        );
    }

    @Test
    public void suffixOperator() {
        assertParse(
                new Expr.Percent(Range.sameLine(0, 2), new Expr.LiteralInt(Range.sameLine(0, 1), 5)),
                ExprParser.suffixOperator(),
                ParsingIterator.of(new Token.LiteralInt(Range.sameLine(0, 1), 5), new Token.Percent(Range.sameLine(1, 2)))
        );

        assertParseFailure(
                UnexpectedInputException.class,
                ExprParser.suffixOperator(),
                ParsingIterator.of(new Token.LiteralBool(Range.sameLine(0, 4), true), new Token.Not(Range.sameLine(4, 5)))
        );
    }

    @Test
    public void multiplicationOperator() {
        //3 * 2
        assertParse(
                new Expr.Mul(
                        Range.sameLine(0, 5),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                        new Expr.LiteralInt(Range.sameLine(4, 5), 2)
                ),
                ExprParser.multiplicationOperator(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.Mul(Range.sameLine(2, 3)),
                        new Token.LiteralInt(Range.sameLine(4, 5), 2)
                )
        );

        //3 / 2
        assertParse(
                new Expr.Div(
                        Range.sameLine(0, 5),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                        new Expr.LiteralInt(Range.sameLine(4, 5), 2)
                ),
                ExprParser.multiplicationOperator(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.Div(Range.sameLine(2, 3)),
                        new Token.LiteralInt(Range.sameLine(4, 5), 2)
                )
        );

        //3 / 2 * 10
        assertParse(
                new Expr.Mul(
                        Range.sameLine(0, 10),
                        new Expr.Div(
                                Range.sameLine(0, 5),
                                new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                                new Expr.LiteralInt(Range.sameLine(4, 5), 2)
                        ),
                        new Expr.LiteralInt(Range.sameLine(8, 10), 10)
                ),
                ExprParser.multiplicationOperator(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.Div(Range.sameLine(2, 3)),
                        new Token.LiteralInt(Range.sameLine(4, 5), 2),
                        new Token.Mul(Range.sameLine(6, 7)),
                        new Token.LiteralInt(Range.sameLine(8, 10), 10)
                )
        );
    }

    @Test
    public void arithmeticOperator() {
        //3 + 2
        assertParse(
                new Expr.Add(
                        Range.sameLine(0, 5),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                        new Expr.LiteralInt(Range.sameLine(4, 5), 2)
                ),
                ExprParser.arithmeticOperator(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.Plus(Range.sameLine(2, 3)),
                        new Token.LiteralInt(Range.sameLine(4, 5), 2)
                )
        );

        //3 - 2
        assertParse(
                new Expr.Sub(
                        Range.sameLine(0, 5),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                        new Expr.LiteralInt(Range.sameLine(4, 5), 2)
                ),
                ExprParser.arithmeticOperator(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.Minus(Range.sameLine(2, 3)),
                        new Token.LiteralInt(Range.sameLine(4, 5), 2)
                )
        );

        //3 - 2 + 10
        assertParse(
                new Expr.Add(
                        Range.sameLine(0, 10),
                        new Expr.Sub(
                                Range.sameLine(0, 5),
                                new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                                new Expr.LiteralInt(Range.sameLine(4, 5), 2)
                        ),
                        new Expr.LiteralInt(Range.sameLine(8, 10), 10)
                ),
                ExprParser.arithmeticOperator(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.Minus(Range.sameLine(2, 3)),
                        new Token.LiteralInt(Range.sameLine(4, 5), 2),
                        new Token.Plus(Range.sameLine(6, 7)),
                        new Token.LiteralInt(Range.sameLine(8, 10), 10)
                )
        );
    }

    @Test
    public void comparisonOperator() {
        //3 == 2
        assertParse(
                new Expr.Equal(
                        Range.sameLine(0, 6),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                        new Expr.LiteralInt(Range.sameLine(5, 6), 2)
                ),
                ExprParser.comparisonOperator(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.Equal(Range.sameLine(2, 4)),
                        new Token.LiteralInt(Range.sameLine(5, 6), 2)
                )
        );

        //3 != 2
        assertParse(
                new Expr.NotEqual(
                        Range.sameLine(0, 6),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                        new Expr.LiteralInt(Range.sameLine(5, 6), 2)
                ),
                ExprParser.comparisonOperator(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.NotEqual(Range.sameLine(2, 4)),
                        new Token.LiteralInt(Range.sameLine(5, 6), 2)
                )
        );

        //3 >= 2
        assertParse(
                new Expr.GreaterEqual(
                        Range.sameLine(0, 6),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                        new Expr.LiteralInt(Range.sameLine(5, 6), 2)
                ),
                ExprParser.comparisonOperator(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.GreaterEqual(Range.sameLine(2, 4)),
                        new Token.LiteralInt(Range.sameLine(5, 6), 2)
                )
        );

        //3 > 2
        assertParse(
                new Expr.Greater(
                        Range.sameLine(0, 5),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                        new Expr.LiteralInt(Range.sameLine(4, 5), 2)
                ),
                ExprParser.comparisonOperator(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.Greater(Range.sameLine(2, 3)),
                        new Token.LiteralInt(Range.sameLine(4, 5), 2)
                )
        );
    }

    @Test
    public void booleanOperator() {
        //true && false
        assertParse(
                new Expr.And(
                        Range.sameLine(0, 13),
                        new Expr.LiteralBool(Range.sameLine(0, 4), true),
                        new Expr.LiteralBool(Range.sameLine(8, 13), false)
                ),
                ExprParser.booleanOperator(),
                ParsingIterator.of(
                        new Token.LiteralBool(Range.sameLine(0, 4), true),
                        new Token.And(Range.sameLine(5, 7)),
                        new Token.LiteralBool(Range.sameLine(8, 13), false)
                )
        );

        //true || false
        assertParse(
                new Expr.Or(
                        Range.sameLine(0, 13),
                        new Expr.LiteralBool(Range.sameLine(0, 4), true),
                        new Expr.LiteralBool(Range.sameLine(8, 13), false)
                ),
                ExprParser.booleanOperator(),
                ParsingIterator.of(
                        new Token.LiteralBool(Range.sameLine(0, 4), true),
                        new Token.Or(Range.sameLine(5, 7)),
                        new Token.LiteralBool(Range.sameLine(8, 13), false)
                )
        );

        //true || false && true
        assertParse(
                new Expr.And(
                        Range.sameLine(0, 21),
                        new Expr.Or(
                                Range.sameLine(0, 13),
                                new Expr.LiteralBool(Range.sameLine(0, 4), true),
                                new Expr.LiteralBool(Range.sameLine(8, 13), false)
                        ),
                        new Expr.LiteralBool(Range.sameLine(17, 21), true)
                ),
                ExprParser.booleanOperator(),
                ParsingIterator.of(
                        new Token.LiteralBool(Range.sameLine(0, 4), true),
                        new Token.Or(Range.sameLine(5, 7)),
                        new Token.LiteralBool(Range.sameLine(8, 13), false),
                        new Token.And(Range.sameLine(14, 16)),
                        new Token.LiteralBool(Range.sameLine(17, 21), true)
                )
        );
    }

    @Test
    public void parenthesized() {
        //(3 * 2)
        assertParse(
                new Expr.Mul(
                        Range.sameLine(1, 6),
                        new Expr.LiteralInt(Range.sameLine(1, 2), 3),
                        new Expr.LiteralInt(Range.sameLine(5, 6), 2)
                ),
                ExprParser.anyExpr(),
                ParsingIterator.of(
                        new Token.ParenthesisOpen(Range.sameLine(0, 1)),
                        new Token.LiteralInt(Range.sameLine(1, 2), 3),
                        new Token.Mul(Range.sameLine(3, 4)),
                        new Token.LiteralInt(Range.sameLine(5, 6), 2),
                        new Token.ParenthesisClosed(Range.sameLine(6, 7))
                )
        );

        //(3) * 2
        assertParse(
                new Expr.Mul(
                        Range.sameLine(1, 7),
                        new Expr.LiteralInt(Range.sameLine(1, 2), 3),
                        new Expr.LiteralInt(Range.sameLine(6, 7), 2)
                ),
                ExprParser.multiplicationOperator(),
                ParsingIterator.of(
                        new Token.ParenthesisOpen(Range.sameLine(0, 1)),
                        new Token.LiteralInt(Range.sameLine(1, 2), 3),
                        new Token.ParenthesisClosed(Range.sameLine(2, 3)),
                        new Token.Mul(Range.sameLine(4, 5)),
                        new Token.LiteralInt(Range.sameLine(6, 7), 2)
                )
        );
    }

    @Test
    public void precedence() {
        //5 + 3 * 4
        assertParse(
                new Expr.Add(
                        Range.sameLine(0, 9),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 5),
                        new Expr.Mul(
                                Range.sameLine(4, 9),
                                new Expr.LiteralInt(Range.sameLine(4, 5), 3),
                                new Expr.LiteralInt(Range.sameLine(8, 9), 4)
                        )
                ),
                ExprParser.anyExpr(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 5),
                        new Token.Plus(Range.sameLine(2, 3)),
                        new Token.LiteralInt(Range.sameLine(4, 5), 3),
                        new Token.Mul(Range.sameLine(6, 7)),
                        new Token.LiteralInt(Range.sameLine(8, 9), 4)
                )
        );

        //3 * -4 + 5
        assertParse(
                new Expr.Add(
                        Range.sameLine(0, 10),
                        new Expr.Mul(
                                Range.sameLine(0, 6),
                                new Expr.LiteralInt(Range.sameLine(0, 1), 3),
                                new Expr.Negation(Range.sameLine(4, 6), new Expr.LiteralInt(Range.sameLine(5, 6), 4))
                        ),
                        new Expr.LiteralInt(Range.sameLine(9, 10), 5)
                ),
                ExprParser.anyExpr(),
                ParsingIterator.of(
                        new Token.LiteralInt(Range.sameLine(0, 1), 3),
                        new Token.Mul(Range.sameLine(2, 3)),
                        new Token.Minus(Range.sameLine(4, 5)),
                        new Token.LiteralInt(Range.sameLine(5, 6), 4),
                        new Token.Plus(Range.sameLine(7, 8)),
                        new Token.LiteralInt(Range.sameLine(9, 10), 5)
                )
        );

        //(5 + 3) * 4
        assertParse(
                new Expr.Mul(
                        Range.sameLine(1, 11),
                        new Expr.Add(
                                Range.sameLine(1, 6),
                                new Expr.LiteralInt(Range.sameLine(1, 2), 5),
                                new Expr.LiteralInt(Range.sameLine(5, 6), 3)
                        ),
                        new Expr.LiteralInt(Range.sameLine(10, 11), 4)
                ),
                ExprParser.anyExpr(),
                ParsingIterator.of(
                        new Token.ParenthesisOpen(Range.sameLine(0, 1)),
                        new Token.LiteralInt(Range.sameLine(1, 2), 5),
                        new Token.Plus(Range.sameLine(3, 4)),
                        new Token.LiteralInt(Range.sameLine(5, 6), 3),
                        new Token.ParenthesisClosed(Range.sameLine(6, 7)),
                        new Token.Mul(Range.sameLine(8, 9)),
                        new Token.LiteralInt(Range.sameLine(10, 11), 4)
                )
        );
    }
}
