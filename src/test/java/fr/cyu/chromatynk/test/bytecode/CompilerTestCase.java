package fr.cyu.chromatynk.test.bytecode;

import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.ast.Statement;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.bytecode.Bytecode;
import fr.cyu.chromatynk.bytecode.Compiler;
import fr.cyu.chromatynk.eval.Value;
import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.Range;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompilerTestCase {

    private void assertCompileExpression(List<Bytecode> expected, Expr expr) {
        List<Bytecode> result = new LinkedList<>();
        Compiler.compileExpression(expr, result);
        assertEquals(expected, result);
    }

    private void assertCompileStatement(List<Bytecode> expected, Statement statement) {
        List<Bytecode> result = new LinkedList<>();
        Compiler.compileStatement(statement, result, 0);
        assertEquals(expected, result);
    }

    @Test
    public void literal() {
        assertCompileExpression(
                List.of(new Bytecode.Push(Range.sameLine(0, 4), new Value.Bool(true))),
                new Expr.LiteralBool(Range.sameLine(0, 4), true)
        );

        assertCompileExpression(
                List.of(new Bytecode.Push(Range.sameLine(0, 3), new Value.Str("abc"))),
                new Expr.LiteralString(Range.sameLine(0, 3), "abc")
        );

        assertCompileExpression(
                List.of(new Bytecode.Push(Range.sameLine(0, 1), new Value.Int(5))),
                new Expr.LiteralInt(Range.sameLine(0, 1), 5)
        );

        assertCompileExpression(
                List.of(new Bytecode.Push(Range.sameLine(0, 3), new Value.Float(0.5))),
                new Expr.LiteralFloat(Range.sameLine(0, 3), 0.5)
        );

        assertCompileExpression(
                List.of(new Bytecode.Push(Range.sameLine(0, 4), new Value.Color(1, 0, 0, 1))),
                new Expr.LiteralColor(Range.sameLine(0, 4), 1, 0, 0, 1)
        );
    }

    @Test
    public void unary() {
        assertCompileExpression(
                List.of(
                        new Bytecode.Push(Range.sameLine(0, 1), new Value.Int(5)),
                        new Bytecode.Percent(Range.sameLine(0, 2))
                ),
                new Expr.Percent(
                        Range.sameLine(0, 2),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 5)
                )
        );

        assertCompileExpression(
                List.of(
                        new Bytecode.Push(Range.sameLine(1, 5), new Value.Bool(true)),
                        new Bytecode.Not(Range.sameLine(0, 5))
                ),
                new Expr.Not(
                        Range.sameLine(0, 5),
                        new Expr.LiteralBool(Range.sameLine(1, 5), true)
                )
        );
    }

    @Test
    public void binary() {
        assertCompileExpression(
                List.of(
                        new Bytecode.Push(Range.sameLine(0, 1), new Value.Int(5)),
                        new Bytecode.Push(Range.sameLine(4, 5), new Value.Int(3)),
                        new Bytecode.Add(Range.sameLine(0, 5))
                ),
                new Expr.Add(
                        Range.sameLine(0, 5),
                        new Expr.LiteralInt(Range.sameLine(0, 1), 5),
                        new Expr.LiteralInt(Range.sameLine(4, 5), 3)
                )
        );

        assertCompileExpression(
                List.of(
                        new Bytecode.Push(Range.sameLine(0, 4), new Value.Bool(true)),
                        new Bytecode.Push(Range.sameLine(8, 13), new Value.Bool(false)),
                        new Bytecode.Or(Range.sameLine(0, 13))
                ),
                new Expr.Or(
                        Range.sameLine(0, 13),
                        new Expr.LiteralBool(Range.sameLine(0, 4), true),
                        new Expr.LiteralBool(Range.sameLine(8, 13), false)
                )
        );
    }

    @Test
    public void varCall() {
        assertCompileExpression(
                List.of(new Bytecode.Load(Range.sameLine(0, 1), "x")),
                new Expr.VarCall(Range.sameLine(0, 1), "x")
        );
    }

    @Test
    public void oneLineStatement() {
        assertCompileStatement(
                List.of(
                        new Bytecode.Push(Range.sameLine(4, 5), new Value.Int(5)),
                        new Bytecode.Forward(Range.sameLine(0, 5))
                ),
                new Statement.Forward(
                        Range.sameLine(0, 5),
                        new Expr.LiteralInt(Range.sameLine(4, 5), 5)
                )
        );

        assertCompileStatement(
                List.of(
                        new Bytecode.Push(Range.sameLine(4, 5), new Value.Int(5)),
                        new Bytecode.Push(Range.sameLine(7, 9), new Value.Int(10)),
                        new Bytecode.Move(Range.sameLine(0, 9))
                ),
                new Statement.Move(
                        Range.sameLine(0, 9),
                        new Expr.LiteralInt(Range.sameLine(4, 5), 5),
                        new Expr.LiteralInt(Range.sameLine(7, 9), 10)
                )
        );
    }

    @Test
    public void declareVariable() {
        //INT x
        assertCompileStatement(
                List.of(
                        new Bytecode.Push(Range.sameLine(0, 5), new Value.Int(0)),
                        new Bytecode.Declare(Range.sameLine(0, 5), Type.INT, "x")
                ),
                new Statement.DeclareVariable(Range.sameLine(0, 5), Type.INT, "x", Optional.empty())
        );

        //INT x = 5
        assertCompileStatement(
                List.of(
                        new Bytecode.Push(Range.sameLine(8, 9), new Value.Int(5)),
                        new Bytecode.Declare(Range.sameLine(0, 9), Type.INT, "x")
                ),
                new Statement.DeclareVariable(
                        Range.sameLine(0, 9),
                        Type.INT,
                        "x",
                        Optional.of(new Expr.LiteralInt(Range.sameLine(8, 9),5))
                )
        );
    }

    @Test
    public void assign() {
        assertCompileStatement(
                List.of(
                        new Bytecode.Push(Range.sameLine(4, 5), new Value.Int(0)),
                        new Bytecode.Store(Range.sameLine(0, 5), "x")
                ),
                new Statement.AssignVariable(
                        Range.sameLine(0, 5),
                        "x",
                        new Expr.LiteralInt(Range.sameLine(4, 5), 0)
                )
        );
    }

    @Test
    public void ifCondition() {

        Range wholeRange = new Range(new Position(0, 0), new Position(1, 4));

        /*
        IF condition {
          FWD 5
        } ELSE {
          FWD 10
        }
         */
        assertCompileStatement(
                List.of(
                        new Bytecode.Load(Range.sameLine(3, 12), "condition"),
                        new Bytecode.GoToIfFalse(wholeRange, 7),
                        new Bytecode.NewScope(new Range(new Position(13, 0), new Position(1, 2))),
                        new Bytecode.Push(Range.sameLine(6, 7, 1), new Value.Int(5)),
                        new Bytecode.Forward(Range.sameLine(2, 7, 1)),
                        new Bytecode.ExitScope(new Range(new Position(13, 0), new Position(1, 2))),
                        new Bytecode.GoTo(wholeRange, 11),
                        new Bytecode.NewScope(new Range(new Position(7, 2), new Position(1, 4))),
                        new Bytecode.Push(Range.sameLine(6, 8, 3), new Value.Int(10)),
                        new Bytecode.Forward(Range.sameLine(2, 8, 3)),
                        new Bytecode.ExitScope(new Range(new Position(7, 2), new Position(1, 4)))
                ),
                new Statement.If(
                        wholeRange,
                        new Expr.VarCall(Range.sameLine(3, 12), "condition"),
                        new Statement.Body(
                                new Range(new Position(13, 0), new Position(1, 2)),
                                List.of(new Statement.Forward(
                                        Range.sameLine(2, 7, 1),
                                        new Expr.LiteralInt(Range.sameLine(6, 7, 1), 5)
                                ))
                        ),
                        Optional.of(new Statement.Body(
                                new Range(new Position(7, 2), new Position(1, 4)),
                                List.of(new Statement.Forward(
                                        Range.sameLine(2, 8, 3),
                                        new Expr.LiteralInt(Range.sameLine(6, 8, 3), 10)
                                ))
                        ))
                )
        );
    }

    //TODO test if/while/for
}
