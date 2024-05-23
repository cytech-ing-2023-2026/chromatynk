package fr.cyu.chromatynk.typing;

import fr.cyu.chromatynk.ast.Expr;

import static fr.cyu.chromatynk.ast.Expr.*;

import fr.cyu.chromatynk.ast.Statement;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A class Typer with a static function inside which handles different cases
 */
public class Typer {

    /**
     * A static function getType which throws an exception with different cases inside
     *
     * @param expr    the expression used
     * @param context the context needed for the expression
     */

    public static Type getType(Expr expr, TypingContext context) throws TypingException {
        return switch (expr) {
            case LiteralBool ignored -> Type.BOOLEAN;
            case LiteralString ignored -> Type.STRING;
            case LiteralInt ignored -> Type.INT;
            case LiteralFloat ignored -> Type.FLOAT;
            case LiteralColor ignored -> Type.COLOR;

            case Percent(Range range, Expr value) -> switch (getType(value, context)) {
                case INT, FLOAT -> Type.PERCENTAGE;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(value.range()), Set.of(Type.INT, Type.FLOAT), actual);
            };

            case Negation(Range range, Expr value) -> switch (getType(value, context)) {
                case INT -> Type.INT;
                case FLOAT -> Type.FLOAT;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(value.range()), Set.of(Type.INT, Type.FLOAT), actual);
            };

            case Add(Range ignored, Expr left, Expr right) -> switch (getType(left, context)) {

                case BOOLEAN -> switch (getType(left, context)) {
                    case STRING -> Type.STRING;
                    case Type actual -> throw new TypeMismatchException(right.range(), Set.of(Type.STRING), actual);
                };

                case STRING -> Type.STRING;

                case INT -> switch (getType(right, context)) {
                    case INT -> Type.INT;
                    case FLOAT -> Type.FLOAT;
                    case STRING -> Type.STRING;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.STRING), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT -> Type.FLOAT;
                    case FLOAT -> Type.FLOAT;
                    case STRING -> Type.STRING;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.STRING), actual);
                };

                case COLOR -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case COLOR -> Type.COLOR;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.COLOR), actual);
                };

                case PERCENTAGE -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case PERCENTAGE -> Type.PERCENTAGE;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.PERCENTAGE), actual);
                };
            };

            case Sub(Range ignored, Expr left, Expr right) -> switch (getType(left, context)) {

                case BOOLEAN -> switch (getType(left, context)) {
                    case STRING -> Type.STRING;
                    case Type actual -> throw new TypeMismatchException(right.range(), Set.of(Type.STRING), actual);
                };

                case STRING -> Type.STRING;

                case INT -> switch (getType(right, context)) {
                    case INT -> Type.INT;
                    case FLOAT -> Type.FLOAT;
                    case STRING -> Type.STRING;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.STRING), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT -> Type.FLOAT;
                    case FLOAT -> Type.FLOAT;
                    case STRING -> Type.STRING;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.STRING), actual);
                };

                case COLOR -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case COLOR -> Type.COLOR;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.COLOR), actual);
                };

                case PERCENTAGE -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case PERCENTAGE -> Type.PERCENTAGE;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.PERCENTAGE), actual);
                };
            };

            case Mul(Range ignored, Expr left, Expr right) -> switch (getType(left, context)) {

                case BOOLEAN ->
                        throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.INT, Type.FLOAT), Type.BOOLEAN);

                case STRING -> Type.STRING;

                case INT -> switch (getType(right, context)) {
                    case INT -> Type.INT;
                    case FLOAT -> Type.FLOAT;
                    case STRING -> Type.STRING;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.STRING), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT -> Type.FLOAT;
                    case FLOAT -> Type.FLOAT;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case COLOR ->
                        throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.INT, Type.FLOAT), Type.COLOR);

                case PERCENTAGE -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case PERCENTAGE -> Type.PERCENTAGE;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.PERCENTAGE), actual);
                };
            };

            case Div(Range ignored, Expr left, Expr right) -> switch (getType(left, context)) {

                case BOOLEAN ->
                        throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.INT, Type.FLOAT), Type.BOOLEAN);

                case STRING -> Type.STRING;

                case INT -> switch (getType(right, context)) {
                    case INT -> Type.INT;
                    case FLOAT -> Type.FLOAT;
                    case STRING -> Type.STRING;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.STRING), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT -> Type.FLOAT;
                    case FLOAT -> Type.FLOAT;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.STRING), actual);
                };

                case COLOR ->
                        throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.INT, Type.FLOAT), Type.COLOR);


                case PERCENTAGE -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case PERCENTAGE -> Type.PERCENTAGE;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.PERCENTAGE), actual);
                };
            };

            case Not(Range range, Expr value) -> switch (getType(value, context)) {
                case INT -> Type.FLOAT;
                case FLOAT -> Type.INT;
                // autres types?
                case Type actual ->
                        throw new TypeMismatchException(range.merge(value.range()), Set.of(Type.INT, Type.FLOAT), actual);
            };

            case Or(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> Type.INT;// il faut aussi l'autre type du or
                case FLOAT -> Type.FLOAT;
                case STRING -> Type.STRING;
                case PERCENTAGE -> Type.PERCENTAGE;
                case COLOR -> Type.COLOR;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENTAGE, Type.COLOR), actual);
            };

            case And(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> Type.INT;// il faut aussi l'autre type du and
                case FLOAT -> Type.FLOAT;
                case STRING -> Type.STRING;
                case PERCENTAGE -> Type.PERCENTAGE;
                case COLOR -> Type.COLOR;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENTAGE, Type.COLOR), actual);
            };

            case Equal(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case PERCENTAGE -> switch (getType(right, context)) {
                    case PERCENTAGE -> Type.BOOLEAN;
                    case INT,FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENTAGE, Type.INT, Type.FLOAT), actual);
                };

                case STRING -> switch (getType(right, context)) {
                    case STRING -> Type.BOOLEAN;
                    case INT,FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENTAGE, Type.INT, Type.FLOAT), actual);
                };

                case COLOR -> switch (getType(right, context)) {
                    case STRING -> Type.BOOLEAN;
                    case INT,FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.COLOR, Type.INT, Type.FLOAT), actual);
                };
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENTAGE, Type.COLOR), actual);
            };

            case NotEqual(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case PERCENTAGE -> switch (getType(right, context)) {
                    case PERCENTAGE -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENTAGE), actual);
                };
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENTAGE, Type.COLOR), actual);
            };

            case Greater(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case PERCENTAGE -> switch (getType(right, context)) {
                    case PERCENTAGE -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENTAGE), actual);
                };
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENTAGE, Type.COLOR), actual);
            };

            case Less(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case PERCENTAGE -> switch (getType(right, context)) {
                    case PERCENTAGE -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENTAGE), actual);
                };
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENTAGE, Type.COLOR), actual);
            };

            case GreaterEqual(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case PERCENTAGE -> switch (getType(right, context)) {
                    case PERCENTAGE -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENTAGE), actual);
                };
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENTAGE, Type.COLOR), actual);
            };

            case LessEqual(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case PERCENTAGE -> switch (getType(right, context)) {
                    case PERCENTAGE -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENTAGE), actual);
                };

                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENTAGE, Type.COLOR), actual);
            };

            case VarCall(Range range, String name) -> context
                    .getType(name)
                    .orElseThrow(() -> new MissingVariableException(range, name));
        };
    }

    public static void assertTypeMatch(Range range, Set<Type> expected, Type actualType) throws TypeMismatchException {
        if (!expected.contains(actualType)) throw new TypeMismatchException(range, expected, actualType);
    }

    /**
     * A void checkTypes which throws an exception with different cases inside
     *
     * @param statement the statement used
     * @param context   the context needed for the instruction
     */
    public static void checkTypes(Statement statement, TypingContext context) throws TypingException {
        switch (statement) {
            case Statement.Body(Range ignored, List<Statement> statements) -> {
                for (Statement stat : statements) {
                    checkTypes(stat, context);
                }
            }
            case Statement.For(
                    Range range, String iterator, Optional<Expr> from, Expr to, Optional<Expr> step, Statement.Body body
            ) -> {

                if(from.isPresent()) assertTypeMatch(from.get().range(), Set.of(Type.INT), getType(from.get(), context));
                assertTypeMatch(to.range(), Set.of(Type.INT), getType(to, context));

                TypingContext forContext = new TypingContext(context, new HashMap<>());
                forContext.declareVariable(iterator, Type.INT, range);

                if (step.isPresent())
                    assertTypeMatch(step.get().range(), Set.of(Type.INT), getType(step.get(), forContext));

                checkTypes(body, forContext);
            }
            case Statement.While(Range ignored, Expr condition, Statement.Body body) -> {
                assertTypeMatch(condition.range(), Set.of(Type.BOOLEAN), getType(condition, context));
                checkTypes(body, new TypingContext(context, new HashMap<>()));
            }
            case Statement.If(Range ignored, Expr condition, Statement.Body ifTrue, Optional<Statement.Body> ifFalse) -> {
                assertTypeMatch(condition.range(), Set.of(Type.BOOLEAN), getType(condition, context));
                checkTypes(ifTrue, new TypingContext(context, new HashMap<>()));
                if(ifFalse.isPresent()) checkTypes(ifFalse.get(), new TypingContext(context, new HashMap<>()));
            }
            case Statement.Forward(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(expr, context));
            case Statement.Backward(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(expr, context));
            case Statement.Turn(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(expr, context));
            case Statement.Pos(Range ignored, Expr x, Expr y) -> {
                assertTypeMatch(x.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(x, context));
                assertTypeMatch(y.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(y, context));
            }
            case Statement.Move(Range ignored, Expr x, Expr y) -> {
                assertTypeMatch(x.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(x, context));
                assertTypeMatch(y.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(y, context));
            }
            case Statement.Press(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(expr, context));
            case Statement.Color(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.COLOR), getType(expr, context));
            case Statement.ColorRGB(Range range, Expr r, Expr g, Expr b) -> {
                Type rType = getType(r, context);
                Type gType = getType(g, context);
                Type bType = getType(b, context);

                boolean nums = rType.isNumeric() && gType.isNumeric() && bType.isNumeric();
                boolean percents = rType == Type.PERCENTAGE && gType == Type.PERCENTAGE && bType == Type.PERCENTAGE;

                if(!nums && !percents) {
                    String actualTypes = rType.getName() + ", " + gType.getName() + ", " + bType.getName();
                    String message = """
                                Only the following combinations are valids for COLOR r, g, b:
                                - INT (0-255), INT (0-255), INT (0-255)
                                - NUM (0-1), NUM (0-1), NUM (0-1).
                                - 0-100%, 0-100%, 0-100%
                                Note: INT can implicitly be converted to NUM so that `COLOR 1.0, 1, 0` works (yellow).""";

                    throw new TypingException(range, message + "\n\nGot: " + actualTypes);
                }

            }
            case Statement.Thick(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(expr, context));
            case Statement.LookAtCursor(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.STRING, Type.INT), getType(expr, context));
            case Statement.LookAtPos(Range ignored, Expr x, Expr y) -> {
                assertTypeMatch(x.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(x, context));
                assertTypeMatch(y.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(y, context));
            }
            case Statement.CreateCursor(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.STRING), getType(expr, context));
            case Statement.SelectCursor(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.STRING), getType(expr, context));
            case Statement.RemoveCursor(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.STRING), getType(expr, context));
            case Statement.Mimic(Range ignored, Expr expr, Statement.Body body) -> {
                assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT), getType(expr, context));
                checkTypes(body, new TypingContext(context, new HashMap<>()));
            }
            case Statement.MirrorCentral(Range ignored, Expr centerX, Expr centerY, Statement.Body body) -> {
                assertTypeMatch(centerX.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(centerX, context));
                assertTypeMatch(centerY.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(centerY, context));
                checkTypes(body, new TypingContext(context, new HashMap<>()));
            }
            case Statement.MirrorAxial(Range ignored, Expr axisStartX, Expr axisStartY, Expr axisEndX, Expr axisEndY, Statement.Body body) -> {
                assertTypeMatch(axisStartX.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(axisStartX, context));
                assertTypeMatch(axisStartY.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(axisStartY, context));
                assertTypeMatch(axisEndX.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(axisEndX, context));
                assertTypeMatch(axisEndY.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), getType(axisEndY, context));
                checkTypes(body, new TypingContext(context, new HashMap<>()));
            }
            case Statement.DeclareVariable(Range range, Type type, String name, Optional<Expr> expr) -> {
                if(expr.isPresent()) assertTypeMatch(expr.get().range(), type == Type.FLOAT ? Set.of(type, Type.INT) : Set.of(type), getType(expr.get(), context));
                context.declareVariable(name, type, range);
            }
            case Statement.AssignVariable(Range range, String name, Expr expr) -> {
                Type type = context.getType(name).orElseThrow(() -> new MissingVariableException(range, name));
                assertTypeMatch(expr.range(), type == Type.FLOAT ? Set.of(type, Type.INT) : Set.of(type), getType(expr, context));
            }
            case Statement.DeleteVariable(Range range, String name) -> context.deleteVariable(name, range);
            case Statement.Hide ignored -> {}
            case Statement.Show ignored -> {}
        }
    }
}


