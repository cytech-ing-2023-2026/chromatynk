package fr.cyu.chromatynk.bytecode;

import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.ast.Program;
import fr.cyu.chromatynk.ast.Statement;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.eval.Value;
import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.Range;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Compiler {

    public static void compileExpression(Expr expr, List<Bytecode> instructions) {
        switch (expr) {
            case Expr.LiteralBool(Range range, boolean value) ->
                    instructions.add(new Bytecode.Push(range, new Value.Bool(value)));

            case Expr.LiteralString(Range range, String value) ->
                    instructions.add(new Bytecode.Push(range, new Value.Str(value)));

            case Expr.LiteralInt(Range range, int value) ->
                    instructions.add(new Bytecode.Push(range, new Value.Int(value)));

            case Expr.LiteralFloat(Range range, double value) ->
                    instructions.add(new Bytecode.Push(range, new Value.Float(value)));

            case Expr.LiteralColor(Range range, double red, double green, double blue, double alpha) ->
                    instructions.add(new Bytecode.Push(range, new Value.Color(red, green, blue, alpha)));

            case Expr.Percent(Range range, Expr expr1) -> {
                compileExpression(expr1, instructions);
                instructions.add(new Bytecode.Percent(range));
            }

            case Expr.Negation(Range range, Expr expr1) -> {
                compileExpression(expr1, instructions);
                instructions.add(new Bytecode.Negation(range));
            }

            case Expr.Not(Range range, Expr expr1) -> {
                compileExpression(expr1, instructions);
                instructions.add(new Bytecode.Not(range));
            }

            case Expr.Add(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.Add(range));
            }

            case Expr.Sub(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.Sub(range));
            }

            case Expr.Mul(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.Mul(range));
            }

            case Expr.Div(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.Div(range));
            }

            case Expr.Or(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.Or(range));
            }

            case Expr.And(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.And(range));
            }

            case Expr.Equal(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.Equal(range));
            }

            case Expr.NotEqual(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.NotEqual(range));
            }

            case Expr.Greater(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.Greater(range));
            }

            case Expr.Less(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.Less(range));
            }

            case Expr.GreaterEqual(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.GreaterEqual(range));
            }

            case Expr.LessEqual(Range range, Expr left, Expr right) -> {
                compileExpression(left, instructions);
                compileExpression(right, instructions);
                instructions.add(new Bytecode.LessEqual(range));
            }

            case Expr.VarCall(Range range, String name) -> instructions.add(new Bytecode.Load(range, name));

        }
    }

    public static void compileStatement(Statement statement, List<Bytecode> instructions, int offset) {
        switch (statement) {
            case Statement.Body(Range range, List<Statement> statements) -> {
                for (Statement stat : statements) compileStatement(stat, instructions, offset);
            }

            case Statement.Forward(Range range, Expr distance) -> {
                compileExpression(distance, instructions);
                instructions.add(new Bytecode.Forward(range));
            }

            case Statement.Backward(Range range, Expr distance) -> {
                compileExpression(distance, instructions);
                instructions.add(new Bytecode.Backward(range));
            }

            /*
            FOR i FROM 0 TO 10 STEP 1 {
              FWD 5
            }

            INT i = 0
            WHILE i < 10 {
              FWD 5
              i = i + 1
            }

            Push(0)
            Declare(Type.INT, "i")
            Load("i") //3
            Push(10)
            Less()
            GoToIfFalse(14)
            Push(5)
            Forward()
            Load("i")
            Push(1)
            Add()
            Store("i")
            GoTo(3)
            ... //14
             */
            case Statement.For(
                    Range range, String iterator, Expr from, Expr to, Optional<Expr> step, Statement.Body body
            ) -> {
                instructions.add(new Bytecode.NewScope(range));

                compileStatement(new Statement.DeclareVariable(
                        range,
                        Type.INT,
                        iterator,
                        Optional.of(from)
                ), instructions, offset);

                int conditionAddr = instructions.size() + offset;

                compileExpression(new Expr.Less(
                        range,
                        new Expr.VarCall(range, iterator),
                        to
                ), instructions);

                int whileAddr = instructions.size() + offset;

                List<Bytecode> bodyInstructions = new LinkedList<>();
                bodyInstructions.add(new Bytecode.NewScope(body.range()));
                compileStatement(body, bodyInstructions, whileAddr + 1);
                bodyInstructions.add(new Bytecode.ExitScope(body.range()));
                compileStatement(new Statement.AssignVariable(
                        range,
                        iterator,
                        new Expr.Add(
                                range,
                                new Expr.VarCall(range, iterator),
                                step.orElse(new Expr.LiteralInt(range, 1))
                        )
                ), bodyInstructions, whileAddr + 1);

                int endAddr = whileAddr + bodyInstructions.size() + 2;

                instructions.add(new Bytecode.GoToIfFalse(range, endAddr));
                instructions.addAll(bodyInstructions);
                instructions.add(new Bytecode.GoTo(range, conditionAddr));
                instructions.add(new Bytecode.ExitScope(range));
            }

            case Statement.Turn(Range range, Expr angle) -> {
                compileExpression(angle, instructions);
                instructions.add(new Bytecode.Turn(range));
            }

            /*
            WHILE condition {
              FWD 5
            }
            ...

            Load("condition") //0
            GoToIfFalse(5) //1
            Push(5)
            Forward()
            GoTo(0)
            ... //5
             */
            case Statement.While(Range range, Expr condition, Statement.Body body) -> {
                int conditionAddr = instructions.size() + offset;
                compileExpression(condition, instructions);

                int whileAddr = instructions.size() + offset;

                List<Bytecode> bodyInstructions = new LinkedList<>();
                bodyInstructions.add(new Bytecode.NewScope(body.range()));
                compileStatement(body, bodyInstructions, whileAddr + 1);
                bodyInstructions.add(new Bytecode.ExitScope(body.range()));

                int endAddr = whileAddr + bodyInstructions.size() + 2;

                instructions.add(new Bytecode.GoToIfFalse(range, endAddr));
                instructions.addAll(bodyInstructions);
                instructions.add(new Bytecode.GoTo(range, conditionAddr));
            }

            case Statement.Pos(Range range, Expr x, Expr y) -> {
                compileExpression(x, instructions);
                compileExpression(y, instructions);
                instructions.add(new Bytecode.Pos(range));
            }

            case Statement.Move(Range range, Expr distanceX, Expr distanceY) -> {
                compileExpression(distanceX, instructions);
                compileExpression(distanceY, instructions);
                instructions.add(new Bytecode.Move(range));
            }

            case Statement.Hide(Range range) -> instructions.add(new Bytecode.Hide(range));

            case Statement.Show(Range range) -> instructions.add(new Bytecode.Show(range));

            case Statement.Press(Range range, Expr opacity) -> {
                compileExpression(opacity, instructions);
                instructions.add(new Bytecode.Press(range));
            }

            case Statement.Color(Range range, Expr color) -> {
                compileExpression(color, instructions);
                instructions.add(new Bytecode.Color(range));
            }

            case Statement.ColorRGB(Range range, Expr red, Expr green, Expr blue) -> {
                compileExpression(red, instructions);
                compileExpression(green, instructions);
                compileExpression(blue, instructions);
                instructions.add(new Bytecode.ColorRGB(range));
            }

            case Statement.Thick(Range range, Expr thickness) -> {
                compileExpression(thickness, instructions);
                instructions.add(new Bytecode.Thick(range));
            }

            case Statement.LookAtCursor(Range range, Expr cursor) -> {
                compileExpression(cursor, instructions);
                instructions.add(new Bytecode.LookAtCursor(range));
            }

            case Statement.LookAtPos(Range range, Expr targetX, Expr targetY) -> {
                compileExpression(targetX, instructions);
                compileExpression(targetY, instructions);
                instructions.add(new Bytecode.LookAtPos(range));
            }

            case Statement.CreateCursor(Range range, Expr id) -> {
                compileExpression(id, instructions);
                instructions.add(new Bytecode.CreateCursor(range));
            }

            case Statement.SelectCursor(Range range, Expr id) -> {
                compileExpression(id, instructions);
                instructions.add(new Bytecode.SelectCursor(range));
            }

            case Statement.RemoveCursor(Range range, Expr id) -> {
                compileExpression(id, instructions);
                instructions.add(new Bytecode.RemoveCursor(range));
            }

            /*
            IF condition {
              FWD 5
            } ELSE {
              FWD 10
            }
            ...

            Load("condition") //0
            GoToIfFalse(6) //1
            Push(5)
            Forward()
            GoTo(8)
            Push(10) //5
            Forward()
            ... //7
             */
            case Statement.If(Range range, Expr condition, Statement.Body ifTrue, Optional<Statement.Body> ifFalse) -> {
                compileExpression(condition, instructions);

                int ifAddr = instructions.size() + offset;

                List<Bytecode> ifTrueInstructions = new LinkedList<>();
                ifTrueInstructions.add(new Bytecode.NewScope(ifTrue.range()));
                compileStatement(ifTrue, ifTrueInstructions, ifAddr + 1);
                ifTrueInstructions.add(new Bytecode.ExitScope(ifTrue.range()));

                int elseAddr = ifAddr + ifTrueInstructions.size() + 2;

                List<Bytecode> ifFalseInstructions = new LinkedList<>();
                ifFalse.ifPresent(ifFalsePresent -> {
                    ifFalseInstructions.add(new Bytecode.NewScope(ifFalsePresent.range()));
                    compileStatement(ifFalsePresent, ifFalseInstructions, elseAddr);
                    ifFalseInstructions.add(new Bytecode.ExitScope(ifFalsePresent.range()));
                });

                int endAddr = elseAddr + ifFalseInstructions.size();

                instructions.add(new Bytecode.GoToIfFalse(range, elseAddr));
                instructions.addAll(ifTrueInstructions);
                instructions.add(new Bytecode.GoTo(range, endAddr));
                instructions.addAll(ifFalseInstructions);
            }

            case Statement.Mimic(Range range, Expr mimicked, Statement.Body body) -> {
                compileExpression(mimicked, instructions);
                instructions.add(new Bytecode.NewScope(range));
                instructions.add(new Bytecode.Mimic(range));
                compileStatement(body, instructions, offset);
                instructions.add(new Bytecode.ExitScope(range));
            }

            case Statement.MirrorCentral(Range range, Expr centerX, Expr centerY, Statement.Body body) -> {
                compileExpression(centerX, instructions);
                compileExpression(centerY, instructions);
                instructions.add(new Bytecode.NewScope(range));
                instructions.add(new Bytecode.MirrorCentral(range));
                compileStatement(body, instructions, offset);
                instructions.add(new Bytecode.ExitScope(range));
            }

            case Statement.MirrorAxial(
                    Range range, Expr axisStartX, Expr axisStartY, Expr axisEndX, Expr axisEndY, Statement.Body body
            ) -> {
                compileExpression(axisStartX, instructions);
                compileExpression(axisStartY, instructions);
                compileExpression(axisEndX, instructions);
                compileExpression(axisEndY, instructions);
                instructions.add(new Bytecode.NewScope(range));
                instructions.add(new Bytecode.MirrorAxial(range));
                compileStatement(body, instructions, offset);
                instructions.add(new Bytecode.ExitScope(range));
            }

            case Statement.DeclareVariable(Range range, Type type, String name, Optional<Expr> value) -> {
                value.ifPresentOrElse(
                        expr -> compileExpression(expr, instructions),
                        () -> instructions.add(new Bytecode.Push(range, type.getDefaultValue()))
                );
                instructions.add(new Bytecode.Declare(range, type, name));
            }

            case Statement.AssignVariable(Range range, String name, Expr value) -> {
                compileExpression(value, instructions);
                instructions.add(new Bytecode.Store(range, name));
            }

            case Statement.DeleteVariable(Range range, String name) -> {
                instructions.add(new Bytecode.Delete(range, name));
            }
        }
    }

    public static List<Bytecode> compileProgram(Program program) {
        List<Bytecode> instructions = new LinkedList<>();

        for (Statement statement : program.statements()) compileStatement(statement, instructions, 0);

        Position endPosition = program.statements().isEmpty() ? new Position(0, 0) : program.statements().getLast().range().to();
        instructions.add(new Bytecode.End(new Range(endPosition, endPosition)));

        return instructions;
    }
}
