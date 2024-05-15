package fr.cyu.chromatynk.typing;

import fr.cyu.chromatynk.ast.Expr;
import static fr.cyu.chromatynk.ast.Expr.*;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;
import java.util.Set;

/**
 * A class Typer with a static function inside which handles different cases
 */
public class Typer {

    /**
     * A static function getType which throws an exception with different cases inside
     *
     * @param expr the expression used
     * @param context the context needed
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

                case BOOLEAN -> throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.INT, Type.FLOAT), Type.BOOLEAN);

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

                case COLOR -> throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.INT, Type.FLOAT), Type.COLOR);

                case PERCENT -> switch (getType(right, context)) {
                    case STRING -> Type.STRING;
                    case PERCENT -> Type.PERCENT;
                    case Type actual ->
                            throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.PERCENT), actual);
                };
            };

            case Div(Range ignored, Expr left, Expr right) -> switch (getType(left, context)) {

                case BOOLEAN -> throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.INT, Type.FLOAT), Type.BOOLEAN);

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

                case COLOR -> throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.INT, Type.FLOAT), Type.COLOR);


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

            case Equal(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> Type.INT;
                case FLOAT -> Type.FLOAT;
                case STRING -> Type.STRING;
                case PERCENT -> Type.PERCENT;
                case COLOR -> Type.COLOR;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
            };

            case NotEqual(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> Type.INT;
                case FLOAT -> Type.FLOAT;
                case STRING -> Type.STRING;
                case PERCENT -> Type.PERCENT;
                case COLOR -> Type.COLOR;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
            };

            case Greater(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> Type.INT;
                case FLOAT -> Type.FLOAT;
                case STRING -> Type.STRING;
                case PERCENT -> Type.PERCENT;
                case COLOR -> Type.COLOR;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
            };

            case Less(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> Type.INT;
                case FLOAT -> Type.FLOAT;
                case STRING -> Type.STRING;
                case PERCENT -> Type.PERCENT;
                case COLOR -> Type.COLOR;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
            };

            case GreaterEqual(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> Type.INT;
                case FLOAT -> Type.FLOAT;
                case STRING -> Type.STRING;
                case PERCENT -> Type.PERCENT;
                case COLOR -> Type.COLOR;
                case Type actual ->
                        throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT, Type.STRING, Type.PERCENT, Type.COLOR), actual);
            };

            case LessEqual(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
                case INT -> Type.INT;
                case FLOAT -> Type.FLOAT;
                case STRING -> Type.STRING;
                case PERCENT -> Type.PERCENT;
                case COLOR -> Type.COLOR;
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
}
