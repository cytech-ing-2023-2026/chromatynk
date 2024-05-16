package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;

import java.util.Set;

import static fr.cyu.chromatynk.ast.Expr.*;

public class Interpreter {

    public static Value getValue(EvalContext context, Expr expr) throws EvalException{
        return switch (expr)  {
            case LiteralBool(Range ignored, boolean value) -> new Value.Bool(value);
            case LiteralString(Range ignored, String value) -> new Value.Str(value);
            case LiteralInt(Range ignored, int value) -> new Value.Int(value);
            case LiteralFloat(Range ignored, double value) -> new Value.Float(value);
            case LiteralColor(Range ignored, double red, double green, double blue, double opacity) -> new Value.Color(red, green, blue, opacity);

            case Percent(Range range, Expr expr1) -> switch(getValue(context, expr1)) {
                case Value.Int(int value) -> new Value.Percentage(value);
                case Value.Float(double value) -> new Value.Percentage(value);
                case Value value -> throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), value.getType());
            };

            case Negation(Range range, Expr expr1) -> switch(getValue(context, expr1)) {
                case Value.Int(int value) -> new Value.Int(-value);
                case Value.Float(double value) -> new Value.Float(-value);
                case Value value -> throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), value.getType());
            };

            case Add(Range ignored, Expr left, Expr right) -> switch (getValue(context, left)) {

                case Value.Bool(boolean leftValue) -> switch (getValue(context, right)) {
                    case Value.Str(String rightValue) -> new Value.Str(leftValue + rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.STRING), rightValue.getType());
                };

                case Value.Int(int leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Int(leftValue + rightValue);
                    case Value.Float(double rightValue) -> new Value.Float(leftValue + rightValue);
                    case Value.Str(String rightValue) -> new Value.Str(leftValue + rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.STRING), rightValue.getType());
                };

                case Value.Float(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Float(leftValue + rightValue);
                    case Value.Float(double rightValue) -> new Value.Float(leftValue + rightValue);
                    case Value.Str(String rightValue) -> new Value.Str(leftValue + rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.STRING), rightValue.getType());
                };

                case Value.Percentage(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Percentage(double rightValue) -> new Value.Percentage(leftValue + rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.PERCENTAGE), rightValue.getType());
                };

                case Value.Str(String leftValue) -> switch (getValue(context, right)) {
                    case Value.Bool(boolean rightValue) -> new Value.Str(leftValue + rightValue);
                    case Value.Int(int rightValue) -> new Value.Str(leftValue + rightValue);
                    case Value.Float(double rightValue) -> new Value.Str(leftValue + rightValue);
                    case Value.Percentage(double rightValue) -> new Value.Str(leftValue + rightValue);
                    case Value.Str(String rightValue) -> new Value.Str(leftValue + rightValue);
                    case Value.Color rightValue -> new Value.Str(leftValue + rightValue);
                };

                case Value.Color(double leftRed, double leftGreen, double leftBlue, double leftAlpha) ->  switch (getValue(context, right)) {
                    case Value.Str(String rightValue) -> new Value.Str(leftRed + rightValue);

                    case Value.Color(double rightRed, double rightGreen, double rightBlue, double rightAlpha) -> {

                        double red = Math.min(1, Math.max(0, leftRed + rightRed));
                        double green = Math.min(1, Math.max(0, leftGreen + rightGreen));
                        double blue = Math.min(1, Math.max(0, leftBlue + rightBlue));
                        double alpha = Math.min(1, Math.max(0, leftAlpha + rightAlpha));

                        yield new Value.Color(red, green, blue, alpha);
                    }
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.STRING, Type.COLOR), rightValue.getType());
                };
            };

            case Sub(Range ignored, Expr left, Expr right) -> switch (getValue(context, left)) {

                case Value.Int(int leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Int(leftValue - rightValue);
                    case Value.Float(double rightValue) -> new Value.Float(leftValue - rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT), rightValue.getType());
                };

                case Value.Float(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Float(leftValue - rightValue);
                    case Value.Float(double rightValue) -> new Value.Float(leftValue - rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT), rightValue.getType());
                };

                case Value.Percentage(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Percentage(double rightValue) -> new Value.Percentage(leftValue - rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.PERCENTAGE), rightValue.getType());
                };

                case Value.Color(double leftRed, double leftGreen, double leftBlue, double leftAlpha) ->  switch (getValue(context, right)) {
                    case Value.Color(double rightRed, double rightGreen, double rightBlue, double rightAlpha) -> {
                        double red = Math.min(1, Math.max(0, leftRed - rightRed));
                        double green = Math.min(1, Math.max(0, leftGreen - rightGreen));
                        double blue = Math.min(1, Math.max(0, leftBlue - rightBlue));
                        double alpha = Math.min(1, Math.max(0, leftAlpha - rightAlpha));
                        yield new Value.Color(red, green, blue, alpha);
                    }
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.COLOR), rightValue.getType());
                };
                case Value leftValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE, Type.COLOR), leftValue.getType());
            };

            case Mul(Range ignored, Expr left, Expr right) -> switch (getValue(context, left)) {

                case Value.Int(int leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Int(leftValue - rightValue);
                    case Value.Float(double rightValue) -> new Value.Float(leftValue - rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT), rightValue.getType());
                };

                case Value.Float(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Float(leftValue - rightValue);
                    case Value.Float(double rightValue) -> new Value.Float(leftValue - rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT), rightValue.getType());
                };

                case Value.Percentage(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Percentage(double rightValue) -> new Value.Percentage(leftValue - rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.PERCENTAGE), rightValue.getType());
                };

                case Value.Color(double leftRed, double leftGreen, double leftBlue, double leftAlpha) ->  switch (getValue(context, right)) {
                    case Value.Color(double rightRed, double rightGreen, double rightBlue, double rightAlpha) -> {
                        double red = Math.min(1, Math.max(0, leftRed - rightRed));
                        double green = Math.min(1, Math.max(0, leftGreen - rightGreen));
                        double blue = Math.min(1, Math.max(0, leftBlue - rightBlue));
                        double alpha = Math.min(1, Math.max(0, leftAlpha - rightAlpha));
                        yield new Value.Color(red, green, blue, alpha);
                    }
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.COLOR), rightValue.getType());
                };
                case Value leftValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE, Type.COLOR), leftValue.getType());
            };

            case Div(Range ignored, Expr left, Expr right) -> switch (getValue(context, left)) {

                case Value.Int(int leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Int(leftValue - rightValue);
                    case Value.Float(double rightValue) -> new Value.Float(leftValue - rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT), rightValue.getType());
                };

                case Value.Float(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Float(leftValue - rightValue);
                    case Value.Float(double rightValue) -> new Value.Float(leftValue - rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT), rightValue.getType());
                };

                case Value.Percentage(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Percentage(double rightValue) -> new Value.Percentage(leftValue - rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.PERCENTAGE), rightValue.getType());
                };

                case Value.Color(double leftRed, double leftGreen, double leftBlue, double leftAlpha) ->  switch (getValue(context, right)) {
                    case Value.Color(double rightRed, double rightGreen, double rightBlue, double rightAlpha) -> {
                        double red = Math.min(1, Math.max(0, leftRed - rightRed));
                        double green = Math.min(1, Math.max(0, leftGreen - rightGreen));
                        double blue = Math.min(1, Math.max(0, leftBlue - rightBlue));
                        double alpha = Math.min(1, Math.max(0, leftAlpha - rightAlpha));
                        yield new Value.Color(red, green, blue, alpha);
                    }
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.COLOR), rightValue.getType());
                };
                case Value leftValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE, Type.COLOR), leftValue.getType());
            };

            case Not(Range range, Expr expr1) -> switch(getValue(context, expr1)) {

                case Value.Bool(boolean value) -> new Value.Bool(!value);
                case Value value -> throw new TypeMismatchException(range, Set.of(Type.BOOLEAN), value.getType());
            };

            case Or(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Bool(boolean leftValue) -> switch (getValue(context, right)) {
                    case Value.Bool(boolean rightValue) -> new Value.Bool(leftValue || rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
                };

                case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
            };

            case And(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Bool(boolean leftValue) -> switch (getValue(context, right)) {
                    case Value.Bool(boolean rightValue) -> new Value.Bool(leftValue && rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
                };

                case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
            };

            case Equal(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Bool(boolean leftValue) -> switch (getValue(context, right)) {
                    case Value.Bool(boolean rightValue) -> new Value.Bool(leftValue == rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
                };

                case Value.Str(String leftValue) -> switch (getValue(context, right)) {
                    case Value.Str(String rightValue) -> new Value.Str(leftValue == rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
                };

            };











            default -> throw new RuntimeException("default"); //TODO remove
        };
    }
}
