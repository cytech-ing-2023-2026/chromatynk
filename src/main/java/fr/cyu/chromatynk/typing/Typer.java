package fr.cyu.chromatynk.typing;

import fr.cyu.chromatynk.ast.Expr;
import static fr.cyu.chromatynk.ast.Expr.*;

import fr.cyu.chromatynk.ast.Statement;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;

import java.util.List;
import java.util.Set;

/**
 * A class Typer with a static function inside which handles different cases
 */
public class Typer {

    /**
     * A static function getType which throws an exception with different cases inside
     *
     * @param expr the expression used
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

            /** reprendre l'égalité */case Equal(Range range, Expr left, Expr right) -> switch (getType(left, context)) {
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

                case STRING -> switch(getType(right, context)) {
                    case STRING -> Type.BOOLEAN;
                    case Type actual ->
                    throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENT), actual);
                };

                case COLOR -> switch(getType(right, context)) {
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
                    case Type actual -> throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case FLOAT -> switch (getType(right, context)) {
                    case INT, FLOAT -> Type.BOOLEAN;
                    case Type actual -> throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.INT, Type.FLOAT), actual);
                };

                case PERCENT -> switch (getType(right, context)) {
                    case PERCENT -> Type.BOOLEAN;
                    case Type actual -> throw new TypeMismatchException(range.merge(right.range()), Set.of(Type.PERCENT), actual);
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


    /**
     * A voic checkTypes which throws an exception with different cases inside
     *
     * @param statement the statement used
     * @param context the context needed for the instruction
     */
    public void checkTypes(Statement statement, TypingContext context) throws TypingException, TypeCheckException {
        switch (statement) {
            case Statement.Body body -> checkBody(body, context);
            case Statement.For forStmt -> checkFor(forStmt, context);
            case Statement.While whileStmt -> checkWhile(whileStmt, context);
            case Statement.If ifStmt -> checkIf(ifStmt, context);
            case Statement.Forward forward -> checkForward(forward, context);
            case Statement.Backward backward -> checkBackward(backward, context);
            case Statement.Turn turn -> checkTurn(turn, context);
            case Statement.Pos pos -> checkPos(pos, context);
            case Statement.Move move -> checkMove(move, context);
            case Statement.Press press -> checkPress(press, context);
            case Statement.Color color -> checkColor(color, context);
            case Statement.ColorRGB colorRGB -> checkColorRGB(colorRGB, context);
            case Statement.Thick thick -> checkThick(thick, context);
            case Statement.LookAtCursor lookAtCursor -> checkLookAtCursor(lookAtCursor, context);
            case Statement.LookAtPos lookAtPos -> checkLookAtPos(lookAtPos, context);
            case Statement.CreateCursor createCursor -> checkCreateCursor(createCursor, context);
            case Statement.SelectCursor selectCursor -> checkSelectCursor(selectCursor, context);
            case Statement.RemoveCursor removeCursor -> checkRemoveCursor(removeCursor, context);
            case Statement.Mimic mimic -> checkMimic(mimic, context);
            case Statement.MirrorCentral mirrorCentral -> checkMirrorCentral(mirrorCentral, context);
            case Statement.MirrorAxial mirrorAxial -> checkMirrorAxial(mirrorAxial, context);
            case Statement.DeclareVariable declareVariable -> checkDeclareVariable(declareVariable, context);
            case Statement.AssignVariable assignVariable -> checkAssignVariable(assignVariable, context);
            case Statement.DeleteVariable deleteVariable -> checkDeleteVariable(deleteVariable, context);
            case Statement.Hide hide -> checkHide(hide, context);
            case Statement.Show show -> checkShow(show, context);
            default -> throw new TypeCheckException("Unsupported statement type: " + statement.getClass().getSimpleName());
        }
    }

    private void checkShow(Statement.Show show, TypingContext context) {
    }

    private void checkHide(Statement.Hide hide, TypingContext context) {
    }

    private void checkDeleteVariable(Statement.DeleteVariable deleteVariable, TypingContext context) {

    }

    private void checkAssignVariable(Statement.AssignVariable assignVariable, TypingContext context) {

    }

    private void checkDeclareVariable(Statement.DeclareVariable declareVariable, TypingContext context) {

    }

    private void checkMirrorAxial(Statement.MirrorAxial mirrorAxial, TypingContext context) {

    }

    private void checkMirrorCentral(Statement.MirrorCentral mirrorCentral, TypingContext context) {

    }

    private void checkMimic(Statement.Mimic mimic, TypingContext context) {

    }

    private void checkRemoveCursor(Statement.RemoveCursor removeCursor, TypingContext context) {

    }

    private void checkSelectCursor(Statement.SelectCursor selectCursor, TypingContext context) {

    }

    private void checkCreateCursor(Statement.CreateCursor createCursor, TypingContext context) {

    }

    private void checkLookAtPos(Statement.LookAtPos lookAtPos, TypingContext context) {

    }

    private void checkLookAtCursor(Statement.LookAtCursor lookAtCursor, TypingContext context) {

    }

    private void checkThick(Statement.Thick thick, TypingContext context) {

    }

    private void checkColorRGB(Statement.ColorRGB colorRGB, TypingContext context) {

    }

    private void checkColor(Statement.Color color, TypingContext context) {

    }

    private void checkPress(Statement.Press press, TypingContext context) {

    }

    private void checkMove(Statement.Move move, TypingContext context) {

    }

    private void checkPos(Statement.Pos pos, TypingContext context) {

    }

    private void checkTurn(Statement.Turn turn, TypingContext context) {

    }

    private void checkBackward(Statement.Backward backward, TypingContext context) {

    }

    private void checkForward(Statement.Forward forward, TypingContext context) {

    }

    private void checkIf(Statement.If ifStmt, TypingContext context) {

    }

    private void checkWhile(Statement.While whileStmt, TypingContext context) {

    }

    private void checkFor(Statement.For forStmt, TypingContext context) {

    }

    private void checkBody(Statement.Body body, TypingContext context) {
        
    }
}
