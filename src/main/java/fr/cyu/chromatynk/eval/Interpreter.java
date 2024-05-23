package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.bytecode.Bytecode;
import fr.cyu.chromatynk.draw.*;
import fr.cyu.chromatynk.util.Range;
import fr.cyu.chromatynk.util.Tuple2;
import fr.cyu.chromatynk.util.Tuple3;

import java.util.List;
import java.util.Set;

/**
 * Interpreter class that evaluates bytecode
 * This class handles different bytecode instructions and performs corresponding operations.
 */
public class Interpreter {

    private static boolean asBoolean(Range range, Value value) throws TypeMismatchException {
        return switch (value) {
            case Value.Bool(boolean v) -> v;
            case Value actual ->
                    throw new TypeMismatchException(range, Set.of(Type.BOOLEAN), actual.getType());
        };
    }

    private static boolean isNumeric(Value value) {
        return value.getType() == Type.INT || value.getType() == Type.FLOAT;
    }

    private static double asNumeric(Range range, Value value) throws TypeMismatchException {
        return switch (value) {
            case Value.Int(int v) -> v;
            case Value.Float(double v) -> v;
            case Value actual ->
                throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
        };
    }

    private static double asPercentage(Range range, Value value, double dimension) throws TypeMismatchException {
        return switch (value) {
            case Value.Percentage(double percent) -> percent/100 * dimension;
            case Value actual ->
                    throw new TypeMismatchException(range, Set.of(Type.PERCENTAGE), actual.getType());
        };
    }

    private static double asNumericOrPercentage(Range range, Value value, double dimension) throws TypeMismatchException {
        return switch (value) {
            case Value.Percentage(double percent) -> percent/100 * dimension;
            case Value num when isNumeric(num) -> asNumeric(range, num);
            case Value actual ->
                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), actual.getType());
        };
    }

    private static Tuple2<Color, Double> asColorAndAlpha(Range range, Value value) throws TypeMismatchException {
        return switch (value) {
            case Value.Color(double red, double green, double blue, double alpha) -> new Tuple2<>(
                    new Color(red, green, blue),
                    alpha
            );
            case Value actual ->
                    throw new TypeMismatchException(range, Set.of(Type.COLOR), actual.getType());
        };
    }

    private static CursorId asCursorId(Range range, Value value) throws TypeMismatchException {
        return switch (value) {
            case Value.Int(int id) -> new CursorId.Int(id);
            case Value.Str(String id) -> new CursorId.Str(id);
            case Value actual ->
                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.STRING), actual.getType());
        };
    }

    /**
     * Evaluates the given bytecode in the provided evaluation context.
     *
     * @param context  the evaluation context containing the state of variables and execution flow.
     * @param bytecode the bytecode instruction to be evaluated.
     * @throws EvalException if an evaluation error occurs, such as missing variables or type mismatches.
     */
    public static void evaluate(EvalContext context, Bytecode bytecode) throws EvalException {
        switch (bytecode) {
            case Bytecode.Push(Range ignored, Value value) -> context.pushValue(value);
            case Bytecode.Load(Range range, String name) -> context.pushValue(context
                    .getValue(name)
                    .orElseThrow(() -> new MissingVariableException(range, name)));

            case Bytecode.Store(Range ignored, String name) -> {

                context.setValue(name, context.popValue());

            }

            case Bytecode.Declare(Range ignored, Type type, String name) -> {

                Value value = context.popValue();


                context.declareVariable(name, new Variable(type, value));
            }
            case Bytecode.Delete(Range ignored, String name) -> context.deleteVariable(name);
            case Bytecode.GoTo(Range ignored, int address) -> context.setNextAddress(address);
            case Bytecode.GoToIfFalse(Range range, int addressFalse) -> {
                if(!asBoolean(range, context.popValue())) context.setNextAddress(addressFalse);
            }
            case Bytecode.NewScope ignored -> context.createScope();
            case Bytecode.ExitScope ignored -> context.exitScope();
            case Bytecode.Percent(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Int(int value) -> new Value.Percentage(value);
                        case Value.Float(double value) -> new Value.Percentage(value);
                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                    }
            );

            case Bytecode.Negation(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Int(int value) -> new Value.Int(-value);
                        case Value.Float(double value) -> new Value.Float(-value);
                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                    }
            );

            case Bytecode.Add(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Bool(boolean right) -> switch (context.popValue()) {
                            //Due to a JDK 21 bug (yes) we cannot deconstruct the left member's fields.
                            case Value.Str left -> new Value.Str(left.value() + right);
                            case Value value ->
                                    throw new TypeMismatchException(range, Set.of(Type.STRING), value.getType());
                        };

                        case Value.Int(int right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Int(left.value() + right);
                            case Value.Float left -> new Value.Float(left.value() + right);
                            case Value.Str left -> new Value.Str(left.value() + right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.STRING), actual.getType());
                        };

                        case Value.Float(double right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Float(left.value() + right);
                            case Value.Float left -> new Value.Float(left.value() + right);
                            case Value.Str left -> new Value.Str(left.value() + right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.STRING), actual.getType());
                        };

                        case Value.Str(String right) -> switch (context.popValue()) {
                            case Value.Bool left -> new Value.Str(left.value() + right);
                            case Value.Int left -> new Value.Str(left.value() + right);
                            case Value.Float left -> new Value.Str(left.value() + right);
                            case Value.Str left -> new Value.Str(left.value() + right);
                            case Value.Color left -> new Value.Str(left + right);
                            case Value.Percentage left -> new Value.Str(left.value() + right);
                        };

                        case Value.Color right -> switch (context.popValue()) {
                            case Value.Str left -> new Value.Str(left.value() + right);
                            case Value.Color left -> {
                                double red = Math.min(255, left.red() + right.red());
                                double green = Math.min(255, left.green() + right.green());
                                double blue = Math.min(255, left.blue() + right.blue());
                                double alpha = Math.min(255, left.alpha() + right.alpha());

                                yield new Value.Color(red, green, blue, alpha);
                            }
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.STRING, Type.COLOR), actual.getType());
                        };

                        case Value.Percentage(double right) -> switch (context.popValue()) {
                            case Value.Str left -> new Value.Str(left.value() + right);
                            case Value.Percentage left -> new Value.Percentage(left.value() + right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.STRING, Type.PERCENTAGE), actual.getType());
                        };
                    }
            );

            case Bytecode.Sub(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Int(int right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Int(left.value() - right);
                            case Value.Float left -> new Value.Float(left.value() - right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Float(double right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Float(left.value() - right);
                            case Value.Float left -> new Value.Float(left.value() - right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Color right -> switch (context.popValue()) {
                            case Value.Color left -> {
                                double red = Math.max(0, left.red() - right.red());
                                double green = Math.max(0, left.green() - right.green());
                                double blue = Math.max(0, left.blue() - right.blue());
                                double alpha = Math.max(0, left.alpha() - right.alpha());

                                yield new Value.Color(red, green, blue, alpha);
                            }
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.COLOR), actual.getType());
                        };

                        case Value.Percentage(double right) -> switch (context.popValue()) {
                            case Value.Percentage left -> new Value.Percentage(left.value() - right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.PERCENTAGE), actual.getType());
                        };

                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.COLOR, Type.PERCENTAGE), actual.getType());
                    }
            );

            case Bytecode.Mul(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Int(int right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Int(left.value() * right);
                            case Value.Float left -> new Value.Float(left.value() * right);
                            case Value.Color left ->
                                    new Value.Color(left.red() * right, left.green() * right, left.blue() * right, left.alpha());
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.COLOR), actual.getType());
                        };

                        case Value.Float(double right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Float(left.value() * right);
                            case Value.Float left -> new Value.Float(left.value() * right);
                            case Value.Color left ->
                                    new Value.Color(left.red() * right, left.green() * right, left.blue() * right, left.alpha());
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.COLOR), actual.getType());
                        };

                        case Value.Color right -> switch (context.popValue()) {
                            case Value.Int left -> {
                                double red = Math.min(255, Math.max(0, left.value() * right.red()));
                                double green = Math.min(255, Math.max(0, left.value() * right.green()));
                                double blue = Math.min(255, Math.max(0, left.value() * right.blue()));
                                double alpha = Math.min(255, Math.max(0, left.value() * right.alpha()));

                                yield new Value.Color(red, green, blue, alpha);
                            }

                            case Value.Float left -> {
                                double red = Math.min(255, Math.max(0, left.value() * right.red()));
                                double green = Math.min(255, Math.max(0, left.value() * right.green()));
                                double blue = Math.min(255, Math.max(0, left.value() * right.blue()));
                                double alpha = Math.min(255, Math.max(0, left.value() * right.alpha()));

                                yield new Value.Color(red, green, blue, alpha);
                            }

                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.COLOR), actual.getType());
                    }
            );

            case Bytecode.Div(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Int(int right) -> {
                            if(right == 0) throw new InvalidExpressionException(range, "You can not divide by 0.");

                            yield switch (context.popValue()) {
                                case Value.Int left -> new Value.Int(left.value() / right);
                                case Value.Float left -> new Value.Float(left.value() / right);
                                case Value.Color left ->
                                        new Value.Color(left.red() / right, left.green() / right, left.blue() / right, left.alpha());
                                case Value actual ->
                                        throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.COLOR), actual.getType());
                            };
                        }

                        case Value.Float(double right) -> {
                            if (right == 0) throw new InvalidExpressionException(range, "You can not divide by 0.");

                            yield switch (context.popValue()) {
                                case Value.Int left -> new Value.Float(left.value() / right);
                                case Value.Float left -> new Value.Float(left.value() / right);
                                case Value.Color left ->
                                        new Value.Color(left.red() / right, left.green() / right, left.blue() / right, left.alpha());
                                case Value actual ->
                                        throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.COLOR), actual.getType());
                            };
                        }

                        case Value.Color right -> {
                            if (right.red() == 0 || right.green() == 0 || right.blue() == 0)
                                throw new InvalidExpressionException(range, "You can not divide by 0.");

                            yield switch (context.popValue()) {
                                case Value.Int left -> {
                                    double red = Math.min(255, Math.max(0, left.value() / right.red()));
                                    double green = Math.min(255, Math.max(0, left.value() / right.green()));
                                    double blue = Math.min(255, Math.max(0, left.value() / right.blue()));
                                    double alpha = Math.min(255, Math.max(0, left.value() / right.alpha()));

                                    yield new Value.Color(red, green, blue, alpha);
                                }

                                case Value.Float left -> {
                                    double red = Math.min(255, Math.max(0, left.value() / right.red()));
                                    double green = Math.min(255, Math.max(0, left.value() / right.green()));
                                    double blue = Math.min(255, Math.max(0, left.value() / right.blue()));
                                    double alpha = Math.min(255, Math.max(0, left.value() / right.alpha()));

                                    yield new Value.Color(red, green, blue, alpha);
                                }

                                case Value actual ->
                                        throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                            };
                        }

                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.COLOR), actual.getType());
                    }
            );

            case Bytecode.Not(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Bool(boolean value) -> new Value.Bool(!value);
                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.BOOLEAN), actual.getType());
                    }
            );

            case Bytecode.Or(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Bool(boolean right) -> switch (context.popValue()) {
                            case Value.Bool left -> new Value.Bool(left.value() || right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.BOOLEAN), actual.getType());
                        };

                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.BOOLEAN), actual.getType());
                    }
            );

            case Bytecode.And(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Bool(boolean right) -> switch (context.popValue()) {
                            case Value.Bool left -> new Value.Bool(left.value() && right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.BOOLEAN), actual.getType());
                        };

                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.BOOLEAN), actual.getType());
                    }
            );

            case Bytecode.Equal(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Bool(boolean right) -> switch (context.popValue()) {
                            case Value.Bool left -> new Value.Bool(left.value() == right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.BOOLEAN), actual.getType());
                        };

                        case Value.Int(int right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() == right);
                            case Value.Float left -> new Value.Bool(left.value() == right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Float(double right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() == right);
                            case Value.Float left -> new Value.Bool(left.value() == right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Str(String right) -> switch (context.popValue()) {
                            case Value.Str left -> new Value.Bool(left.value().equals(right));
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.STRING), actual.getType());
                        };

                        case Value.Color right -> switch (context.popValue()) {
                            case Value.Color left -> {
                                boolean colorsEqual = left.red() == right.red()
                                        && left.green() == right.green()
                                        && left.blue() == right.blue()
                                        && left.alpha() == right.alpha();

                                yield new Value.Bool(colorsEqual);
                            }
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.COLOR), actual.getType());
                        };

                        case Value.Percentage(double right) -> switch (context.popValue()) {
                            case Value.Percentage left -> new Value.Bool(left.value() == right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.PERCENTAGE), actual.getType());
                        };
                    }
            );

            case Bytecode.NotEqual(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Bool(boolean right) -> switch (context.popValue()) {
                            case Value.Bool left -> new Value.Bool(left.value() != right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.BOOLEAN), actual.getType());
                        };

                        case Value.Int(int right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() != right);
                            case Value.Float left -> new Value.Bool(left.value() != right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Float(double right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() != right);
                            case Value.Float left -> new Value.Bool(left.value() != right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Str(String right) -> switch (context.popValue()) {
                            case Value.Str left -> new Value.Bool(!left.value().equals(right));
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.STRING), actual.getType());
                        };

                        case Value.Color right -> switch (context.popValue()) {
                            case Value.Color left -> {
                                boolean isNotEqual = left.red() != right.red()
                                        || left.green() != right.green()
                                        || left.blue() != right.blue()
                                        || left.alpha() != right.alpha();

                                yield new Value.Bool(isNotEqual);
                            }
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.COLOR), actual.getType());
                        };

                        case Value.Percentage(double right) -> switch (context.popValue()) {
                            case Value.Percentage left -> new Value.Bool(left.value() != right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.PERCENTAGE), actual.getType());
                        };
                    }
            );

            case Bytecode.Greater(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Int(int right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() > right);
                            case Value.Float left -> new Value.Bool(left.value() > right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Float(double right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() > right);
                            case Value.Float left -> new Value.Bool(left.value() > right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Percentage(double right) -> switch (context.popValue()) {
                            case Value.Percentage left -> new Value.Bool(left.value() > right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.PERCENTAGE), actual.getType());
                        };

                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), actual.getType());
                    }
            );

            case Bytecode.Less(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Int(int right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() < right);
                            case Value.Float left -> new Value.Bool(left.value() < right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Float(double right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() < right);
                            case Value.Float left -> new Value.Bool(left.value() < right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Percentage(double right) -> switch (context.popValue()) {
                            case Value.Percentage left -> new Value.Bool(left.value() < right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.PERCENTAGE), actual.getType());
                        };

                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), actual.getType());
                    }
            );

            case Bytecode.GreaterEqual(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Int(int right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() >= right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT), actual.getType());
                        };

                        case Value.Float(double right) -> switch (context.popValue()) {
                            case Value.Float left -> new Value.Bool(left.value() >= right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.FLOAT), actual.getType());
                        };

                        case Value.Percentage(double right) -> switch (context.popValue()) {
                            case Value.Percentage left -> new Value.Bool(left.value() >= right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.PERCENTAGE), actual.getType());
                        };

                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), actual.getType());
                    }
            );

            case Bytecode.LessEqual(Range range) -> context.pushValue(
                    switch (context.popValue()) {
                        case Value.Int(int right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() <= right);
                            case Value.Float left -> new Value.Bool(left.value() <= right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Float(double right) -> switch (context.popValue()) {
                            case Value.Int left -> new Value.Bool(left.value() <= right);
                            case Value.Float left -> new Value.Bool(left.value() <= right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), actual.getType());
                        };

                        case Value.Percentage(double right) -> switch (context.popValue()) {
                            case Value.Percentage left -> new Value.Bool(left.value() <= right);
                            case Value actual ->
                                    throw new TypeMismatchException(range, Set.of(Type.PERCENTAGE), actual.getType());
                        };

                        case Value actual ->
                                throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE), actual.getType());
                    }
            );

            case Bytecode.Forward(Range range) -> context
                    .getCurrentCursor()
                    .move(context.getGraphics(), asNumericOrPercentage(range, context.popValue(), context.getLargestDimension()));

            case Bytecode.Backward(Range range) -> context
                    .getCurrentCursor()
                    .move(context.getGraphics(), asNumericOrPercentage(range, context.popValue(), -context.getLargestDimension()));

            case Bytecode.Turn(Range range) -> context.getCurrentCursor().turn(asNumeric(range, context.popValue()));

            case Bytecode.Pos(Range range) -> {
                context.getCurrentCursor().setY(asNumericOrPercentage(range, context.popValue(), context.getHeight()));
                context.getCurrentCursor().setX(asNumericOrPercentage(range, context.popValue(), context.getWidth()));
            }

            case Bytecode.Move(Range range) -> {
                double dy = asNumericOrPercentage(range, context.popValue(), context.getHeight());
                double dx = asNumericOrPercentage(range, context.popValue(), context.getWidth());
                context.getCurrentCursor().move(context.getGraphics(), dx, dy);
            }

            case Bytecode.Hide ignored -> context.getCurrentCursor().setVisible(false);
            case Bytecode.Show ignored -> context.getCurrentCursor().setVisible(true);

            case Bytecode.Press(Range range) -> {
                double value = asNumericOrPercentage(range, context.popValue(), 1);
                if(value<0 || value>1) throw new InvalidExpressionException(range, "Opacity must be between 0 / 0% and 1 / 100%");
                context.getCurrentCursor().setOpacity(value);
            }


            case Bytecode.Color(Range range) -> {
                Tuple2<Color, Double> colorAndAlpha = asColorAndAlpha(range, context.popValue());
                context.getCurrentCursor().setColor(colorAndAlpha.a());
                context.getCurrentCursor().setOpacity(colorAndAlpha.b());
            }

            case Bytecode.ColorRGB(Range range) -> {
                Color color = switch(new Tuple3<>(context.popValue(), context.popValue(), context.popValue())) {
                    case Tuple3(Value.Percentage(double blue), Value.Percentage(double green), Value.Percentage(double red)) when (
                            red >= 0 && red <= 100
                            && green >= 0 && green <= 100
                            && blue >= 0 && blue <= 100
                    ) ->
                            new Color(red/100, green/100, blue/100);

                    case Tuple3(Value.Int(int blue), Value.Int(int green), Value.Int(int red)) when (
                            red >= 0 && red <= 255
                            && green >= 0 && green <= 255
                            && blue >= 0 && blue <= 255
                    ) ->
                            new Color(red/255.0, green/255.0, blue/255.0);

                    case Tuple3(Value blue, Value green, Value red) when (
                            isNumeric(blue) && isNumeric(green) && isNumeric(red)
                            && asNumeric(range, red) >= 0 && asNumeric(range, red) <= 1
                            && asNumeric(range, green) >= 0 && asNumeric(range, green) <= 1
                            && asNumeric(range, blue) >= 0 && asNumeric(range, blue) <= 1
                    ) ->
                            new Color(asNumeric(range, red), asNumeric(range, green), asNumeric(range, blue));

                    case Tuple3(Value blue, Value green, Value red) -> {
                        String actualTypes = red.getType().getName() + ", " + green.getType().getName() + ", " + blue.getType().getName();
                        String message = """
                                Only the following combinations are valids for COLOR r, g, b:
                                - INT (0-255), INT (0-255), INT (0-255)
                                - NUM (0-1), NUM (0-1), NUM (0-1).
                                - 0-100%, 0-100%, 0-100%
                                Note: INT can implicitly be converted to NUM so that `COLOR 1.0, 1, 0` works (yellow).""";

                        throw new EvalException(range, message + "\n\nGot: " + actualTypes);
                    }
                };

                context.getCurrentCursor().setColor(color);
            }

            case Bytecode.Thick(Range range) -> {
                double value = asNumeric(range, context.popValue());
                if(value<0) throw new InvalidExpressionException(range, "Thickness must be positive");
                context.getCurrentCursor().setThickness(value);
            }

            case Bytecode.LookAtCursor(Range range) -> {
                Cursor current = context.getCurrentCursor();
                CursorId targetId = asCursorId(range, context.popValue());
                Cursor target = context
                        .getCursor(targetId)
                        .orElseThrow(() -> new MissingCursorException(range, targetId));

                //Translation vector from `current` to `target`
                double dx = target.getX()-current.getX();
                double dy = target.getY()-current.getY();

                //Length of translation vector (dx, dy)
                double length = Math.sqrt(dx*dx + dy*dy);

                //Direction = unit vector of the same direction as (dx, dy) = (dx, dy)/length
                current.setDirX(dx/length);
                current.setDirY(dy/length);
            }

            case Bytecode.LookAtPos(Range range) -> {
                Cursor current = context.getCurrentCursor();
                double targetY = asNumericOrPercentage(range, context.popValue(), context.getHeight());
                double targetX = asNumericOrPercentage(range, context.popValue(), context.getWidth());

                //Translation vector from `current` to (targetX, targetY)
                double dx = targetX-current.getX();
                double dy = targetY-current.getY();

                //Length of translation vector (dx, dy)
                double length = Math.sqrt(dx*dx + dy*dy);

                //Direction = unit vector of the same direction as (dx, dy) = (dx, dy)/length
                current.setDirX(dx/length);
                current.setDirY(dy/length);
            }

            case Bytecode.CreateCursor(Range range) ->
                    context.declareCursor(asCursorId(range, context.popValue()), new TangibleCursor(
                            context.getWidth()/2,
                            context.getHeight()/2
                    ));

            case Bytecode.SelectCursor(Range range) -> context.selectCursorId(asCursorId(range, context.popValue()));

            case Bytecode.RemoveCursor(Range range) -> context.deleteCursor(asCursorId(range, context.popValue()));

            case Bytecode.Mimic(Range range) -> {
                CursorId mimickedId = asCursorId(range, context.popValue());
                context.declareCursor(
                        mimickedId,
                        MimickedCursor.at(
                                context.getCursor(mimickedId).orElseThrow(() -> new MissingCursorException(range, mimickedId)),
                                context.getCurrentCursor().getX(),
                                context.getCurrentCursor().getY()
                        )
                );
            }

            case Bytecode.MirrorCentral(Range range) -> {
                double centerY = asNumericOrPercentage(range, context.popValue(), context.getHeight());
                double centerX = asNumericOrPercentage(range, context.popValue(), context.getWidth());

                context.declareCursor(
                        context.getCurrentCursorId(),
                        new CentralMirroredCursor(context.getCurrentCursor(), centerX, centerY)
                );
            }

            case Bytecode.MirrorAxial(Range range) -> {
                double lineBY = asNumericOrPercentage(range, context.popValue(), context.getHeight());
                double lineBX = asNumericOrPercentage(range, context.popValue(), context.getWidth());
                double lineAY = asNumericOrPercentage(range, context.popValue(), context.getHeight());
                double lineAX = asNumericOrPercentage(range, context.popValue(), context.getWidth());


                context.declareCursor(
                        context.getCurrentCursorId(),
                        new AxialMirroredCursor(context.getCurrentCursor(), lineAX, lineAY, lineBX, lineBY)
                );
            }

            case Bytecode.End(Range range) -> {}
        }
    }

    public static EvalContext evaluateAll(EvalContext context, Clock clock) throws EvalException {
        while (context.hasNext() && clock.tick(context.peek().isEffectful())){
            evaluate(context, context.next());
        }
        return context;
    }
}
