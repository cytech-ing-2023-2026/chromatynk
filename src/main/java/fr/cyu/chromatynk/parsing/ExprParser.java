package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.util.Range;
import fr.cyu.chromatynk.util.TriFunction;

import java.util.Map;
import java.util.function.BiFunction;

import static fr.cyu.chromatynk.parsing.CommonParser.anyToken;
import static fr.cyu.chromatynk.parsing.CommonParser.tokenOf;

/**
 * The expression parser of Chromat'ynk. Transforms tokens into AST expressions.
 */
public class ExprParser {

    private static double parseColorComponent(String hex, double max) {
        return Integer.parseInt(hex, 16) / max;
    }

    private static Expr.LiteralColor parseHexColor(Range range, String hex) throws UnexpectedInputException {
        return switch (hex.length()) {
            case 3 -> new Expr.LiteralColor(
                    range,
                    parseColorComponent(hex.substring(0, 1), 15),
                    parseColorComponent(hex.substring(1, 2), 15),
                    parseColorComponent(hex.substring(2, 3), 15),
                    1
            );

            case 4 -> new Expr.LiteralColor(
                    range,
                    parseColorComponent(hex.substring(0, 1), 15),
                    parseColorComponent(hex.substring(1, 2), 15),
                    parseColorComponent(hex.substring(2, 3), 15),
                    parseColorComponent(hex.substring(3, 4), 15)
            );

            case 6 -> new Expr.LiteralColor(
                    range,
                    parseColorComponent(hex.substring(0, 2), 255),
                    parseColorComponent(hex.substring(2, 4), 255),
                    parseColorComponent(hex.substring(4, 6), 255),
                    1
            );

            case 8 -> new Expr.LiteralColor(
                    range,
                    parseColorComponent(hex.substring(0, 2), 255),
                    parseColorComponent(hex.substring(2, 4), 255),
                    parseColorComponent(hex.substring(4, 6), 255),
                    parseColorComponent(hex.substring(6, 8), 255)
            );

            default -> throw new UnexpectedInputException(range, "RGB/RGBA hexadecimal color", hex);
        };
    }

    /**
     * Literal parser.
     */
    public static Parser<Token, Expr> literal() {
        return anyToken().map(token -> switch (token) {
            case Token.LiteralBool(Range range, boolean value) -> new Expr.LiteralBool(range, value);
            case Token.LiteralString(Range range, String value) -> new Expr.LiteralString(range, value);
            case Token.LiteralInt(Range range, int value) -> new Expr.LiteralInt(range, value);
            case Token.LiteralFloat(Range range, double value) -> new Expr.LiteralFloat(range, value);
            case Token.LiteralColor(Range range, String hex) -> parseHexColor(range, hex.substring(1));
            default -> throw new UnexpectedInputException(token.range(), "Literal value", token.toPrettyString());
        });
    }

    /**
     * Variable call parser.
     */
    public static Parser<Token, Expr> varCall() {
        return tokenOf(Token.Identifier.class).map(id -> new Expr.VarCall(id.range(), id.name()));
    }

    /**
     * Parenthesized expression parser.
     */
    public static Parser<Token, Expr> parenthesized() {
        return tokenOf(Token.ParenthesisOpen.class, "(")
                .zip(Parser.lazy(ExprParser::anyExpr))
                .zip(tokenOf(Token.ParenthesisClosed.class, ")"))
                .map(result -> result.a().b());
    }

    /**
     * Invokable expression parser. Either a literal, variable call or parenthesized expression.
     */
    public static Parser<Token, Expr> invokable() {
        return Parser
                .firstSucceeding(literal(), varCall(), parenthesized())
                .mapError(e -> new ParsingException.NonFatal(e.getRange(), "Illegal invokable expression"));
    }

    private static final Map<Class<? extends Token.Operator>, BiFunction<Range, Expr, Expr>> PREFIX_OPS = Map.ofEntries(
            Map.entry(Token.Plus.class, (ignored, expr) -> expr),
            Map.entry(Token.Minus.class, Expr.Negation::new),
            Map.entry(Token.Not.class, Expr.Not::new)
    );

    private static final Map<Class<? extends Token.Operator>, BiFunction<Range, Expr, Expr>> SUFFIX_OPS = Map.ofEntries(
            Map.entry(Token.Percent.class, Expr.Percent::new)
    );

    private static final Map<Class<? extends Token.Operator>, TriFunction<Range, Expr, Expr, Expr>> BOOLEAN_OPS = Map.ofEntries(
            Map.entry(Token.And.class, Expr.And::new),
            Map.entry(Token.Or.class, Expr.Or::new)
    );

    private static final Map<Class<? extends Token.Operator>, TriFunction<Range, Expr, Expr, Expr>> COMPARISON_OPS = Map.ofEntries(
            Map.entry(Token.Equal.class, Expr.Equal::new),
            Map.entry(Token.NotEqual.class, Expr.NotEqual::new),
            Map.entry(Token.Greater.class, Expr.Greater::new),
            Map.entry(Token.Less.class, Expr.Less::new),
            Map.entry(Token.GreaterEqual.class, Expr.GreaterEqual::new),
            Map.entry(Token.LessEqual.class, Expr.LessEqual::new)
    );

    private static final Map<Class<? extends Token.Operator>, TriFunction<Range, Expr, Expr, Expr>> ARITHMETIC_OPS = Map.ofEntries(
            Map.entry(Token.Plus.class, Expr.Add::new),
            Map.entry(Token.Minus.class, Expr.Sub::new)
    );

    private static final Map<Class<? extends Token.Operator>, TriFunction<Range, Expr, Expr, Expr>> MULTIPLICATION_OPS = Map.ofEntries(
            Map.entry(Token.Mul.class, Expr.Mul::new),
            Map.entry(Token.Div.class, Expr.Div::new),
            Map.entry(Token.Mod.class, Expr.Modulo::new)
    );

    private static Expr parseUnaryOperator(Token.Operator opToken, Expr expr, String opType, Map<Class<? extends Token.Operator>, BiFunction<Range, Expr, Expr>> operators) throws ParsingException {
        if (operators.containsKey(opToken.getClass()))
            return operators.get(opToken.getClass()).apply(opToken.range().merge(expr.range()), expr);
        else
            throw new UnexpectedInputException(opToken.range(), opType + " operator", "Operator \"" + opToken.toPrettyString() + "\"");
    }

    /**
     * Prefix operator parser.
     */
    public static Parser<Token, Expr> prefixOperator() {
        return tokenOf(Token.Operator.class)
                .zip(invokable())
                .map(tpl -> parseUnaryOperator(tpl.a(), tpl.b(), "Prefix", PREFIX_OPS));
    }

    /**
     * Suffix operator parser.
     */
    public static Parser<Token, Expr> suffixOperator() {
        return invokable()
                .zip(tokenOf(Token.Operator.class))
                .map(tpl -> parseUnaryOperator(tpl.b(), tpl.a(), "Suffix", SUFFIX_OPS));
    }

    /**
     * Unary (prefix or suffix) operator parser. Can parse an invocable without suffix/prefix operator.
     */
    public static Parser<Token, Expr> unaryOperator() {
        return Parser
                .firstSucceeding(prefixOperator(), suffixOperator(), invokable())
                .mapError(e -> new ParsingException.NonFatal(e.getRange(), "Illegal prefixed/suffixed invokable expression"));
    }

    private static Parser<Token, Expr> binaryOperatorParser(Parser<Token, Expr> operand, String opType, Map<Class<? extends Token.Operator>, TriFunction<Range, Expr, Expr, Expr>> operators) {
        return operand.repeatReduce(tokenOf(Token.Operator.class).map(opToken -> {
            if (operators.containsKey(opToken.getClass()))
                return (left, right) -> operators.get(opToken.getClass()).apply(left.range().merge(right.range()), left, right);
            else
                throw new UnexpectedInputException(opToken.range(), opType + " operator", "Operator \"" + opToken.toPrettyString() + "\"");
        }));
    }

    /**
     * Multiplication/Division parser. Has higher precedence than arithmetic operators.
     */
    public static Parser<Token, Expr> multiplicationOperator() {
        return binaryOperatorParser(unaryOperator(), "* or /", MULTIPLICATION_OPS);
    }

    /**
     * Addition/Subtraction parser. Has higher precedence than comparison operators.
     */
    public static Parser<Token, Expr> arithmeticOperator() {
        return binaryOperatorParser(multiplicationOperator(), "+ or -", ARITHMETIC_OPS);
    }

    /**
     * Comparison operator parser. Has higher precedence than boolean operators.
     */
    public static Parser<Token, Expr> comparisonOperator() {
        return binaryOperatorParser(arithmeticOperator(), "Comparison", COMPARISON_OPS);
    }

    /**
     * And/Or parser (`Not` is a prefix operator). Lowest precedence.
     */
    public static Parser<Token, Expr> booleanOperator() {
        return binaryOperatorParser(comparisonOperator(), "Boolean", BOOLEAN_OPS);
    }

    /**
     * Any expression parser.
     */
    public static Parser<Token, Expr> anyExpr() {
        return booleanOperator().mapError(e -> new ParsingException.NonFatal(e.getRange(), "Illegal start of expression"));
    }
}
