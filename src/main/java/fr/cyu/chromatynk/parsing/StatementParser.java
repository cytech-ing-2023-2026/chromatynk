package fr.cyu.chromatynk.parsing;


import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.ast.Program;
import fr.cyu.chromatynk.ast.Statement;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.QuadriFunction;
import fr.cyu.chromatynk.util.Range;
import fr.cyu.chromatynk.util.TriFunction;
import fr.cyu.chromatynk.util.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static fr.cyu.chromatynk.parsing.CommonParser.anyToken;
import static fr.cyu.chromatynk.parsing.CommonParser.tokenOf;

/**
 * The statement parser of Chromat'ynk. Transforms tokens into AST statements.
 */
public class StatementParser {

    private static final Map<Class<? extends Token>, Function<Range, Statement>> ZERO_ARG_STATEMENTS = Map.ofEntries(
            Map.entry(Token.Hide.class, Statement.Hide::new),
            Map.entry(Token.Show.class, Statement.Show::new)
    );

    private static final Map<Class<? extends Token>, BiFunction<Range, Expr, Statement>> ONE_ARG_STATEMENTS = Map.ofEntries(
            Map.entry(Token.Fwd.class, Statement.Forward::new),
            Map.entry(Token.Bwd.class, Statement.Backward::new),
            Map.entry(Token.Turn.class, Statement.Turn::new),
            Map.entry(Token.Press.class, Statement.Press::new),
            Map.entry(Token.Color.class, Statement.Color::new),
            Map.entry(Token.Thick.class, Statement.Thick::new),
            Map.entry(Token.LookAt.class, Statement.LookAtCursor::new),
            Map.entry(Token.Cursor.class, Statement.CreateCursor::new),
            Map.entry(Token.Select.class, Statement.SelectCursor::new),
            Map.entry(Token.Remove.class, Statement.RemoveCursor::new)
    );

    private static final Map<Class<? extends Token>, TriFunction<Range, Expr, Expr, Statement>> TWO_ARG_STATEMENTS = Map.ofEntries(
            Map.entry(Token.Mov.class, Statement.Move::new),
            Map.entry(Token.Pos.class, Statement.Pos::new),
            Map.entry(Token.LookAt.class, Statement.LookAtPos::new)
    );

    private static final Map<Class<? extends Token>, QuadriFunction<Range, Expr, Expr, Expr, Statement>> THREE_ARG_STATEMENTS = Map.ofEntries(
            Map.entry(Token.Color.class, Statement.ColorRGB::new)
    );

    /**
     * Zero-argument instruction parser.
     */
    public static Parser<Token, Statement> zeroArg() {
        return anyToken().map(token -> {
            if (ZERO_ARG_STATEMENTS.containsKey(token.getClass()))
                return ZERO_ARG_STATEMENTS.get(token.getClass()).apply(token.range());
            else
                throw new ParsingException.Fatal(new UnexpectedInputException(token.range(), "0-arg statement", token.toPrettyString()));
        });
    }

    /**
     * One-argument instruction parser.
     */
    public static Parser<Token, Statement> oneArg() {
        return anyToken().zip(ExprParser.anyExpr()).map(tpl -> {
            Token token = tpl.a();
            Expr expr = tpl.b();
            Range range = token.range().merge(expr.range());
            if (ONE_ARG_STATEMENTS.containsKey(token.getClass()))
                return ONE_ARG_STATEMENTS.get(token.getClass()).apply(range, expr);
            else
                throw new ParsingException.Fatal(new UnexpectedInputException(token.range(), "1-arg statement", token.toPrettyString()));
        });
    }

    /**
     * Two-arguments instruction parser.
     */
    public static Parser<Token, Statement> twoArgs() {
        return anyToken()
                .zip(ExprParser.anyExpr())
                .suffixed(tokenOf(Token.Comma.class))
                .zip(ExprParser.anyExpr().fatal())
                .map(tpl -> {
                    Token token = tpl.a().a();
                    Expr first = tpl.a().b();
                    Expr second = tpl.b();
                    Range range = token.range().merge(first.range()).merge(second.range());
                    if (TWO_ARG_STATEMENTS.containsKey(token.getClass()))
                        return TWO_ARG_STATEMENTS.get(token.getClass()).apply(range, first, second);
                    else
                        throw new ParsingException.Fatal(new UnexpectedInputException(token.range(), "2-args statement", token.toPrettyString()));
                });
    }

    /**
     * Three-argument instruction parser.
     */
    public static Parser<Token, Statement> threeArgs() {
        return anyToken()
                .zip(ExprParser.anyExpr())
                .suffixed(tokenOf(Token.Comma.class))
                .zip(ExprParser.anyExpr().fatal())
                .suffixed(tokenOf(Token.Comma.class))
                .zip(ExprParser.anyExpr().fatal())
                .map(tpl -> {
                    Token token = tpl.a().a().a();
                    Expr first = tpl.a().a().b();
                    Expr second = tpl.a().b();
                    Expr third = tpl.b();
                    Range range = token.range().merge(first.range()).merge(second.range());
                    if (THREE_ARG_STATEMENTS.containsKey(token.getClass()))
                        return THREE_ARG_STATEMENTS.get(token.getClass()).apply(range, first, second, third);
                    else
                        throw new ParsingException.Fatal(new UnexpectedInputException(token.range(), "3-args statement", token.toPrettyString()));
                });
    }

    /**
     * Variable deletion parser.
     */
    public static Parser<Token, Statement> deleteVariable() {
        return tokenOf(Token.Del.class)
                .zip(tokenOf(Token.Identifier.class))
                .map(tpl -> new Statement.DeleteVariable(tpl.a().range().merge(tpl.b().range()), tpl.b().name()));
    }

    /**
     * Variable declaration (and optional assignment) parser.
     */
    public static Parser<Token, Statement> variableDeclaration() {
        return type()
                .zip(tokenOf(Token.Identifier.class).fatal())
                .zip(ExprParser.anyExpr().fatal().prefixed(tokenOf(Token.Assign.class)).optional())
                .map(tpl -> switch (tpl) {
                    case Tuple2(
                            Tuple2(Tuple2(Range typeRange, Type type), Token.Identifier identifier),
                            Optional<Expr> initialExpr
                    ) -> {
                        Range endingRange = initialExpr.map(Expr::range).orElse(identifier.range());
                        yield new Statement.DeclareVariable(typeRange.merge(endingRange), type, identifier.name(), initialExpr);
                    }
                });
    }

    /**
     * Variable assignment parser.
     */
    public static Parser<Token, Statement> variableAssignment() {
        return tokenOf(Token.Identifier.class)
                .zip(ExprParser.anyExpr().fatal().prefixed(tokenOf(Token.Assign.class)))
                .map(tpl -> switch (tpl) {
                    case Tuple2(Token.Identifier id, Expr value) ->
                            new Statement.AssignVariable(id.range().merge(value.range()), id.name(), value);
                });
    }

    /**
     * Parser of any instruction.
     */
    public static Parser<Token, Statement> instruction() {
        return Parser
                .firstSucceeding(deleteVariable(), variableDeclaration(), variableAssignment(), threeArgs(), twoArgs(), oneArg(), zeroArg())
                .mapError(e -> new ParsingException.NonFatal(e.getRange(), "Illegal instruction"));
    }

    /**
     * Body `-> ...` parser.
     */
    public static Parser<Token, Statement.Body> oneLineBody() {
        return tokenOf(Token.Arrow.class)
                .zip(Parser.lazy(StatementParser::anyStatement).fatal())
                .map(tpl -> new Statement.Body(tpl.a().range().merge(tpl.b().range()), List.of(tpl.b())));
    }

    /**
     * Body `{...}` parser.
     */
    public static Parser<Token, Statement.Body> multiLineBody() {
        return tokenOf(Token.BraceOpen.class, "{")
                .zip(Parser.lazy(
                        () -> anyStatement()
                                .fatal()
                                .repeatUntil(Parser.firstSucceeding(tokenOf(Token.EndOfFile.class, "EOF"), tokenOf(Token.BraceClosed.class, "}")))
                ))
                .zip(tokenOf(Token.BraceClosed.class).mapError(e -> new ParsingException.Fatal(e.getRange(), "Missing closing brace `}`")))
                .map(tpl -> switch (tpl) {
                    case Tuple2(Tuple2(Token.BraceOpen open, List<Statement> statements), Token.BraceClosed closed) ->
                            new Statement.Body(open.range().merge(closed.range()), statements);
                });
    }

    public static Parser<Token, Statement.Body> body(){
        return Parser.firstSucceeding(oneLineBody(), multiLineBody())
                .mapError(e -> new ParsingException.NonFatal(e.getRange(), "Invalid body"));
    }

    /**
     * WHILE loop parser.
     */
    public static Parser<Token, Statement> whileLoop() {
        return tokenOf(Token.While.class)
                .zip(ExprParser.anyExpr().fatal())
                .zip(body().fatal())
                .map(tpl -> switch (tpl) {
                    case Tuple2(Tuple2(Token.While whileToken, Expr condition), Statement.Body body) ->
                            new Statement.While(whileToken.range().merge(body.range()), condition, body);
                });
    }

    /**
     * FOR loop parser.
     */
    public static Parser<Token, Statement> forLoop() {
        return tokenOf(Token.For.class)
                .zip(tokenOf(Token.Identifier.class).fatal())
                .zip(ExprParser.anyExpr().fatal().prefixed(tokenOf(Token.From.class)).optional())
                .zip(ExprParser.anyExpr().prefixed(tokenOf(Token.To.class)).fatal())
                .zip(ExprParser.anyExpr().fatal().prefixed(tokenOf(Token.Step.class)).optional())
                .zip(body().fatal())
                .map(tpl -> switch (tpl) {
                    case Tuple2(
                            Tuple2(
                                    Tuple2(
                                            Tuple2(Tuple2(Token forToken, Token.Identifier iterator), Optional<Expr> start),
                                            Expr to
                                    ), Optional<Expr> step
                            ), Statement.Body body
                    ) ->
                            new Statement.For(forToken.range().merge(body.range()), iterator.name(), start, to, step, body);
                });
    }

    /**
     * IF conditional parser.
     */
    public static Parser<Token, Statement> ifCondition() {
        return tokenOf(Token.If.class)
                .zip(ExprParser.anyExpr().fatal())
                .zip(body().fatal())
                .zip(tokenOf(Token.Else.class).zip(body().fatal()).optional())
                .map(tpl -> switch (tpl) {
                    case Tuple2(
                            Tuple2(Tuple2(Token ifToken, Expr condition), Statement.Body ifTrue),
                            Optional<Tuple2<Token.Else, Statement.Body>> elseBody
                    ) -> {
                        Optional<Statement.Body> ifFalse = elseBody.map(Tuple2::b);
                        yield new Statement.If(ifToken.range().merge(ifFalse.orElse(ifTrue).range()), condition, ifTrue, ifFalse);
                    }
                });
    }

    /**
     * MIMIC cursor {...} parser
     */
    public static Parser<Token, Statement> mimic() {
        return tokenOf(Token.Mimic.class)
                .zip(ExprParser.anyExpr().fatal())
                .zip(body().fatal())
                .map(tpl -> switch (tpl) {
                    case Tuple2(Tuple2(Token mimicToken, Expr mimicked), Statement.Body body) ->
                            new Statement.Mimic(mimicToken.range().merge(body.range()), mimicked, body);
                });
    }

    /**
     * MIRROR centerX, centerY {...} parser
     */
    public static Parser<Token, Statement> mirrorCentral() {
        return tokenOf(Token.Mirror.class)
                .zip(ExprParser.anyExpr().fatal())
                .zip(ExprParser.anyExpr().prefixed(tokenOf(Token.Comma.class)).fatal())
                .zip(body().fatal())
                .map(tpl -> switch (tpl) {
                    case Tuple2(Tuple2(Tuple2(Token mirrorToken, Expr centerX), Expr centerY), Statement.Body body) ->
                            new Statement.MirrorCentral(mirrorToken.range().merge(body.range()), centerX, centerY, body);
                });
    }

    /**
     * MIRROR axisStartX, axisStartY, axisEndX, axisEndY {...} parser
     */
    public static Parser<Token, Statement> mirrorAxial() {
        return tokenOf(Token.Mirror.class)
                .zip(ExprParser.anyExpr().fatal())
                .zip(ExprParser.anyExpr().prefixed(tokenOf(Token.Comma.class)).fatal())
                .zip(ExprParser.anyExpr().fatal().prefixed(tokenOf(Token.Comma.class)))
                .zip(ExprParser.anyExpr().fatal().prefixed(tokenOf(Token.Comma.class)))
                .zip(body().fatal())
                .map(tpl -> switch (tpl) {
                    case Tuple2(
                            Tuple2(
                                    Tuple2(
                                            Tuple2(Tuple2(Token mirrorToken, Expr axisStartX), Expr axisStartY),
                                            Expr axisEndX
                                    ), Expr axisEndY
                            ), Statement.Body body
                    ) ->
                            new Statement.MirrorAxial(mirrorToken.range().merge(body.range()), axisStartX, axisStartY, axisEndX, axisEndY, body);
                });
    }

    /**
     * Type (BOOL, NUM...) parser.
     */
    public static Parser<Token, Tuple2<Range, Type>> type() {
        return tokenOf(Token.Identifier.class)
                .map(id -> Type
                        .fromName(id.name())
                        .map(tpe -> new Tuple2<>(id.range(), tpe))
                        .orElseThrow(() -> new UnexpectedInputException(id.range(), "Existing type", id.toPrettyString()))
                );
    }

    /**
     * Any statement parser.
     */
    public static Parser<Token, Statement> anyStatement() {
        return Parser
                .firstSucceeding(whileLoop(), forLoop(), ifCondition(), mimic(), mirrorAxial(), mirrorCentral(), instruction())
                .mapError(e -> new ParsingException.NonFatal(e.getRange(), "Illegal statement"));
    }

    /**
     * Parser of a full program.
     */
    public static Parser<Token, Program> program() {
        return anyStatement().fatal().repeatUntil(tokenOf(Token.EndOfFile.class)).map(Program::new);
    }
}
