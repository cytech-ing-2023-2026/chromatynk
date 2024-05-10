package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.util.Range;

import static fr.cyu.chromatynk.parsing.Parser.*;

public class ExprParser {

    public static final Parser<Token, Expr> LITERAL = Parser.<Token>any().map(token -> switch(token) {
        case Token.LiteralBool(Range range, boolean value) -> new Expr.LiteralBool(value);
        default -> throw new UnexpectedInputException(token.range().from(), "Literal value", token.toString());
    });
}
