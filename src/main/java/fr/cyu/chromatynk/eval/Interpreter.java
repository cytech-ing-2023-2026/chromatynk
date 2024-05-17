package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.util.Range;

import java.util.Set;

import static fr.cyu.chromatynk.ast.Expr.*;

/**
 * Interpreter class to evaluate expressions in a given context.
 */
public class Interpreter {

    /**
     * Evaluates an expression and returns its value
     *
     * @param context   the evaluation context
     * @param expr      the expression to evaluate
     * @return          the value of the evaluated expression
     * @throws EvalException    if there is an error during evaluation
     */
    public static Value getValue(EvalContext context, Expr expr) throws EvalException{
        return switch (expr)  {
            case LiteralBool(Range ignored, boolean value) -> new Value.Bool(value);
            case LiteralString(Range ignored, String value) -> new Value.Str(value);
            case LiteralInt(Range ignored, int value) -> new Value.Int(value);
            case LiteralFloat(Range ignored, double value) -> new Value.Float(value);
            case LiteralColor(Range ignored, double red, double green, double blue, double opacity) -> new Value.Color(red, green, blue, opacity);

            /**
             * Evaluates a percentage expression.
             *
             * @param range     the range of the expression.
             * @param expr1     the expression to evaluate.
             * @return          the percentage value.
             * @throws TypeMismatchException if the type of the expression is not INT or FLOAT.
             */
            case Percent(Range range, Expr expr1) -> switch(getValue(context, expr1)) {
                case Value.Int(int value) -> new Value.Percentage(value);
                case Value.Float(double value) -> new Value.Percentage(value);
                case Value value -> throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), value.getType());
            };

            /**
             * Evaluates a negation expression.
             *
             * @param range     the range of the expression.
             * @param expr1     the expression to evaluate.
             * @return          the negative value of the expression.
             * @throws TypeMismatchException if the type of the expression is not INT or FLOAT.
             */
            case Negation(Range range, Expr expr1) -> switch(getValue(context, expr1)) {
                case Value.Int(int value) -> new Value.Int(-value);
                case Value.Float(double value) -> new Value.Float(-value);
                case Value value -> throw new TypeMismatchException(range, Set.of(Type.INT, Type.FLOAT), value.getType());
            };

            /**
             * Evaluates an addition.
             *
             * @param ignored   the range of the expression.
             * @param left      the left expression.
             * @param right     the right expression.
             * @return          the sum of the two expressions.
             * @throws TypeMismatchException if the types of the expressions do not match the expected types.
             */
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

            /**
             * Evaluates a subtraction expression.
             *
             * @param range     the range of the expression.
             * @param left      the left expression.
             * @param right     the right expression.
             * @return          the difference of the two expressions.
             * @throws TypeMismatchException if the types of the expressions do not match the expected types.
             */
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

            /**
             * Evaluates a multiplication expression.
             *
             * @param range     the range of the expression.
             * @param left      the left expression.
             * @param right     the right expression.
             * @return          the product of the two expressions.
             * @throws TypeMismatchException if the types of the expressions do not match the expected types.
             */
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

            /**
             * Evaluates a division expression.
             *
             * @param range     the range of the expression.
             * @param left      the left expression.
             * @param right     the right expression.
             * @return          the quotient of the two expressions.
             * @throws TypeMismatchException if the types of the expressions do not match the expected types.
             */
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

            /**
             * Evaluates a logical 'Not' expression.
             *
             * @param range     The range in the source code where this expression occurs.
             * @param expr1     The expression to be negated.
             * @return          A new Value.Bool with the negated boolean value.
             * @throws TypeMismatchException if the expression is not a boolean.
             */
            case Not(Range range, Expr expr1) -> switch(getValue(context, expr1)) {

                case Value.Bool(boolean value) -> new Value.Bool(!value);
                case Value value -> throw new TypeMismatchException(range, Set.of(Type.BOOLEAN), value.getType());
            };

            /**
             * Evaluates a logical 'Or' expression.
             *
             * @param range     The range in the source code where this expression occurs.
             * @param left      The left-hand side expression.
             * @param right     The right-hand side expression.
             * @return          A new Value.Bool representing the logical OR of the two boolean values.
             * @throws TypeMismatchException if either expression is not a boolean.
             */
            case Or(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Bool(boolean leftValue) -> switch (getValue(context, right)) {
                    case Value.Bool(boolean rightValue) -> new Value.Bool(leftValue || rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
                };

                case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
            };

            /**
             * Evaluates a logical 'And' expression.
             *
             * @param range     The range in the source code where this expression occurs.
             * @param left      The left-hand side expression.
             * @param right     The right-hand side expression.
             * @return          A new Value.Bool representing the logical AND of the two boolean values.
             * @throws TypeMismatchException if either expression is not a boolean.
             */
            case And(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Bool(boolean leftValue) -> switch (getValue(context, right)) {
                    case Value.Bool(boolean rightValue) -> new Value.Bool(leftValue && rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
                };

                case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
            };

            /**
             * Evaluates an equality expression.
             *
             * @param range     The range in the source code where this expression occurs.
             * @param left      The left-hand side expression.
             * @param right     The right-hand side expression.
             * @return          A new Value.Bool representing whether the two values are equal.
             * @throws TypeMismatchException if the types of the two expressions do not match.
             */
            case Equal(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Bool(boolean leftValue) -> switch (getValue(context, right)) {
                    case Value.Bool(boolean rightValue) -> new Value.Bool(leftValue == rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
                };

                case Value.Int(int leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Bool(leftValue == rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT), rightValue.getType());
                };

                case Value.Float(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Float(double rightValue) -> new Value.Bool(leftValue == rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.FLOAT), rightValue.getType());
                };

                case Value.Percentage(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Percentage(double rightValue) -> new Value.Bool(leftValue == rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.PERCENTAGE), rightValue.getType());
                };

                case Value.Str(String leftValue) -> switch (getValue(context, right)) {
                    case Value.Str(String rightValue) -> new Value.Bool(leftValue.equals(rightValue));
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.STRING), rightValue.getType());
                };

                case Value.Color(double leftRed, double leftGreen, double leftBlue, double leftAlpha) ->  switch (getValue(context, right)) {
                    case Value.Color(double rightRed, double rightGreen, double rightBlue, double rightAlpha) -> {
                        boolean colorsEqual = (leftRed == rightRed) && (leftGreen == rightGreen) && (leftBlue == rightBlue) && (leftAlpha == rightAlpha);
                        yield colorsEqual;
                    }
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.COLOR), rightValue.getType());
                };
            };

            /**
             * Evaluates a non-equality expression.
             *
             * @param range     The range in the source code where this expression occurs.
             * @param left      The left-hand side expression.
             * @param right     The right-hand side expression.
             * @return          A new Value.Bool representing whether the two values are not equal.
             * @throws TypeMismatchException if the types of the two expressions do not match.
             */
            case NotEqual(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Bool(boolean leftValue) -> switch (getValue(context, right)) {
                    case Value.Bool(boolean rightValue) -> new Value.Bool(leftValue != rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.BOOLEAN), rightValue.getType());
                };

                case Value.Int(int leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Bool(leftValue != rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT), rightValue.getType());
                };

                case Value.Float(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Float(double rightValue) -> new Value.Bool(leftValue != rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.FLOAT), rightValue.getType());
                };

                case Value.Percentage(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Percentage(double rightValue) -> new Value.Bool(leftValue != rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.PERCENTAGE), rightValue.getType());
                };

                case Value.Str(String leftValue) -> switch (getValue(context, right)) {
                    case Value.Str(String rightValue) -> new Value.Bool(!leftValue.equals(rightValue));
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.STRING), rightValue.getType());
                };

                case Value.Color(double leftRed, double leftGreen, double leftBlue, double leftAlpha) ->  switch (getValue(context, right)) {
                    case Value.Color(double rightRed, double rightGreen, double rightBlue, double rightAlpha) -> {
                        boolean colorsEqual = (leftRed != rightRed) && (leftGreen != rightGreen) && (leftBlue != rightBlue) && (leftAlpha != rightAlpha);
                        yield colorsEqual;
                    }
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.COLOR), rightValue.getType());
                };
            };

            /**
             * Evaluates a greater-than expression.
             *
             * @param range     The range in the source code where this expression occurs.
             * @param left      The left-hand side expression.
             * @param right     The right-hand side expression.
             * @return          A new Value.Bool representing whether the left value is greater than the right value.
             * @throws TypeMismatchException if the types of the two expressions do not match.
             */
            case Greater(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Int(int leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Bool(leftValue > rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT), rightValue.getType());
                };

                case Value.Float(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Float(double rightValue) -> new Value.Bool(leftValue > rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.FLOAT), rightValue.getType());
                };

                case Value.Percentage(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Percentage(double rightValue) -> new Value.Bool(leftValue > rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.PERCENTAGE), rightValue.getType());
                };

                case Value.Color(double leftRed, double leftGreen, double leftBlue, double leftAlpha) ->  switch (getValue(context, right)) {
                    case Value.Color(double rightRed, double rightGreen, double rightBlue, double rightAlpha) -> {
                        boolean colorsEqual = (leftRed > rightRed) && (leftGreen > rightGreen) && (leftBlue > rightBlue) && (leftAlpha > rightAlpha);
                        yield colorsEqual;
                    }
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.COLOR), rightValue.getType());
                };

                case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE, Type.COLOR), rightValue.getType());
            };

            /**
             * Evaluates a less-than expression.
             *
             * @param range     The range in the source code where this expression occurs.
             * @param left      The left-hand side expression.
             * @param right     The right-hand side expression.
             * @return          A new Value.Bool representing whether the left value is less than the right value.
             * @throws TypeMismatchException if the types of the two expressions do not match.
             */
            case Less(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Int(int leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Bool(leftValue < rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT), rightValue.getType());
                };

                case Value.Float(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Float(double rightValue) -> new Value.Bool(leftValue < rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.FLOAT), rightValue.getType());
                };

                case Value.Percentage(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Percentage(double rightValue) -> new Value.Bool(leftValue < rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.PERCENTAGE), rightValue.getType());
                };

                case Value.Color(double leftRed, double leftGreen, double leftBlue, double leftAlpha) ->  switch (getValue(context, right)) {
                    case Value.Color(double rightRed, double rightGreen, double rightBlue, double rightAlpha) -> {
                        boolean colorsEqual = (leftRed < rightRed) && (leftGreen < rightGreen) && (leftBlue < rightBlue) && (leftAlpha < rightAlpha);
                        yield colorsEqual;
                    }
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.COLOR), rightValue.getType());
                };

                case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE, Type.COLOR), rightValue.getType());
            };

            /**
             * Evaluates a greater-than-or-equal-to expression.
             *
             * @param range     The range in the source code where this expression occurs.
             * @param left      The left-hand side expression.
             * @param right     The right-hand side expression.
             * @return          A new Value.Bool representing whether the left value is greater than or equal to the right value.
             * @throws TypeMismatchException if the types of the two expressions do not match.
             */
            case GreaterEqual(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Int(int leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Bool(leftValue >= rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT), rightValue.getType());
                };

                case Value.Float(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Float(double rightValue) -> new Value.Bool(leftValue >= rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.FLOAT), rightValue.getType());
                };

                case Value.Percentage(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Percentage(double rightValue) -> new Value.Bool(leftValue >= rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.PERCENTAGE), rightValue.getType());
                };

                case Value.Color(double leftRed, double leftGreen, double leftBlue, double leftAlpha) ->  switch (getValue(context, right)) {
                    case Value.Color(double rightRed, double rightGreen, double rightBlue, double rightAlpha) -> {
                        boolean colorsEqual = (leftRed >= rightRed) && (leftGreen >= rightGreen) && (leftBlue >= rightBlue) && (leftAlpha >= rightAlpha);
                        yield colorsEqual;
                    }
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.COLOR), rightValue.getType());
                };

                case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE, Type.COLOR), rightValue.getType());
            };

            /**
             * Evaluates a less-than-or-equal-to expression.
             *
             * @param range     The range in the source code where this expression occurs.
             * @param left      The left-hand side expression.
             * @param right     The right-hand side expression.
             * @return          A new Value.Bool representing whether the left value is less than or equal to the right value.
             * @throws TypeMismatchException if the types of the two expressions do not match.
             */
            case LessEqual(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {

                case Value.Int(int leftValue) -> switch (getValue(context, right)) {
                    case Value.Int(int rightValue) -> new Value.Bool(leftValue <= rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT), rightValue.getType());
                };

                case Value.Float(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Float(double rightValue) -> new Value.Bool(leftValue <= rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.FLOAT), rightValue.getType());
                };

                case Value.Percentage(double leftValue) -> switch (getValue(context, right)) {
                    case Value.Percentage(double rightValue) -> new Value.Bool(leftValue <= rightValue);
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.PERCENTAGE), rightValue.getType());
                };

                case Value.Color(double leftRed, double leftGreen, double leftBlue, double leftAlpha) ->  switch (getValue(context, right)) {
                    case Value.Color(double rightRed, double rightGreen, double rightBlue, double rightAlpha) -> {
                        boolean colorsEqual = (leftRed <= rightRed) && (leftGreen <= rightGreen) && (leftBlue <= rightBlue) && (leftAlpha <= rightAlpha);
                        yield colorsEqual;
                    }
                    case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.COLOR), rightValue.getType());
                };

                case Value rightValue -> throw new TypeMismatchException(right.range(), Set.of(Type.INT, Type.FLOAT, Type.PERCENTAGE, Type.COLOR), rightValue.getType());
            };

            case VarCall(Range range, Expr left, Expr right) -> switch(getValue(context, left)) {



            };

            default -> throw new RuntimeException("default"); //TODO remove
        };
    }
}
