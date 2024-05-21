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
                case INT, FLOAT -> Type.PERCENT;
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

                case PERCENT -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case PERCENT -> Type.PERCENT;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.PERCENT), actual);
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

                case PERCENT -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case PERCENT -> Type.PERCENT;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.PERCENT), actual);
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

                case PERCENT -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case PERCENT -> Type.PERCENT;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.PERCENT), actual);
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


                case PERCENT -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case PERCENT -> Type.PERCENT;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.PERCENT), actual);
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
                case PERCENT -> Type.PERCENT;
                case COLOR -> Type.COLOR;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
            };

            case And(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> Type.INT;// il faut aussi l'autre type du and
                case FLOAT -> Type.FLOAT;
                case STRING -> Type.STRING;
                case PERCENT -> Type.PERCENT;
                case COLOR -> Type.COLOR;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
            };

            /** reprendre l'égalité */
            case Equal(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.FLOAT;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case PERCENT -> switch (getType(right, context)) {
                    case PERCENT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENT), actual);
                };

                case STRING -> switch (getType(right, context)) {
                    case STRING -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENT), actual);
                };

                case COLOR -> switch (getType(right, context)) {
                    case STRING -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.COLOR), actual);
                };
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
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

                case PERCENT -> switch (getType(right, context)) {
                    case PERCENT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENT), actual);
                };
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
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

                case PERCENT -> switch (getType(right, context)) {
                    case PERCENT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENT), actual);
                };
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
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

                case PERCENT -> switch (getType(right, context)) {
                    case PERCENT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENT), actual);
                };
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
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

                case PERCENT -> switch (getType(right, context)) {
                    case PERCENT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENT), actual);
                };
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
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

                case PERCENT -> switch (getType(right, context)) {
                    case PERCENT -> Type.BOOLEAN;
                    case Type actual ->
                            throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENT), actual);
                };

                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
            };

            case VarCall(Range range, String name) -> context
                    .getType(name)
                    .orElseThrow(() -> new MissingVariableException(range, name));

            case Color(Range range, Expr value) -> switch (getType(value, context)) {
                case INT -> Type.INT;
                case FLOAT -> Type.FLOAT;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(value.range()), Set.of(Type.INT, Type.FLOAT), actual);
            };
        };
    }

    public void assertTypeMatch(Range range, Set<Type> expected, Type actualType) {
        if (!expected.contains(actualType)) throw new TypeMismatchException(range, expected, actualType)
    }


    /**
     * A voic checkTypes which throws an exception with different cases inside
     *
     * @param statement the statement used
     * @param context   the context needed for the instruction
     */
    public void checkTypes(Statement statement, TypingContext context) throws TypingException {
        switch (statement) {
            case Statement.Body(Range ignored, List<Statement> statements) -> {
                for (Statement stat : statements) {
                    checkTypes(stat, context);
                }
            }
            case Statement.For(
                    Range range, String iterator, Expr from, Expr to, Optional<Expr> step, Statement.Body body
            ) -> {
                Type fromType = getType(from, context);
                Type toType = getType(to, context);

                assertTypeMatch(from.range(), Set.of(Type.INT, Type.FLOAT), fromType);
                assertTypeMatch(to.range(), Set.of(Type.INT, Type.FLOAT), toType);

                TypingContext forContext = new TypingContext(context, new HashMap<>());
                forContext.declareVariable(iterator, fromType, range);

                if (step.isPresent())
                    assertTypeMatch(step.get().range(), Set.of(Type.INT, Type.FLOAT), getType(step.get(), forContext));

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
            case Statement.Forward(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(expr, context));
            case Statement.Backward(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(expr, context));
            case Statement.Turn(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(expr, context));
            case Statement.Pos(Range ignored, Expr x, Expr y) -> {
                assertTypeMatch(x.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(x, context));
                assertTypeMatch(y.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(y, context));
            }
            case Statement.Move(Range ignored, Expr x, Expr y) -> {
                assertTypeMatch(x.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(x, context));
                assertTypeMatch(y.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(y, context));
            }
            case Statement.Press(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(expr, context));
            case Statement.Color(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.COLOR), getType(expr, context));
            case Statement.ColorRGB(Range ignored, Expr expr) -> throw new RuntimeException("todo");
            case Statement.Thick(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(expr, context));
            case Statement.LookAtCursor(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.STRING), getType(expr, context));
            case Statement.LookAtPos(Range ignored, Expr x, Expr y) -> {
                assertTypeMatch(x.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(x, context));
                assertTypeMatch(y.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENT), getType(y, context));
            }
            case Statement.CreateCursor(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.STRING), getType(expr, context));
            case Statement.SelectCursor(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.STRING), getType(expr, context));
            case Statement.RemoveCursor(Range ignored, Expr expr) -> assertTypeMatch(expr.range(), Set.of(Type.INT, Type.STRING), getType(expr, context));
            case Statement.Mimic(Range ignored, Expr expr, Statement.Body body) -> {
                assertTypeMatch(expr.range(), Set.of(Type.INT, Type.STRING), getType(expr, context));
            }
            case Statement.Mimic mimic -> checkMimic(mimic, context);
            case Statement.MirrorCentral mirrorCentral -> checkMirrorCentral(mirrorCentral, context);
            case Statement.MirrorAxial mirrorAxial -> checkMirrorAxial(mirrorAxial, context);
            case Statement.DeclareVariable declareVariable -> checkDeclareVariable(declareVariable, context);
            case Statement.AssignVariable assignVariable -> checkAssignVariable(assignVariable, context);
            case Statement.DeleteVariable deleteVariable -> checkDeleteVariable(deleteVariable, context);
            case Statement.Hide hide -> checkHide(hide, context);
            case Statement.Show show -> checkShow(show, context);
            default ->
                    throw new TypeCheckException("Unsupported statement type: " + statement.getClass().getSimpleName());
        }
    }
}


