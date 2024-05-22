package fr.cyu.chromatynk.test;

import fr.cyu.chromatynk.Chromatynk;
import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.ast.Program;
import fr.cyu.chromatynk.ast.Statement;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.parsing.ParsingException;
import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.Range;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTestCase {

    @Test
    public void parsing() throws ParsingException {
        String source = """
                INT n = 5
                INT r = 1
                FOR i FROM 2 TO n {
                  r = r*i
                }""";

        Program expected = new Program(List.of(
                new Statement.DeclareVariable(
                        Range.sameLine(0, 9),
                        Type.INT,
                        "n",
                        Optional.of(new Expr.LiteralInt(Range.sameLine(8, 9), 5))
                ),
                new Statement.DeclareVariable(
                        Range.sameLine(0, 9, 1),
                        Type.INT,
                        "r",
                        Optional.of(new Expr.LiteralInt(Range.sameLine(8, 9, 1), 1))
                ),
                new Statement.For(
                        new Range(new Position(0, 2), new Position(1, 4)),
                        "i",
                        new Expr.LiteralInt(Range.sameLine(11, 12, 2), 2),
                        new Expr.VarCall(Range.sameLine(16, 17, 2), "n"),
                        Optional.empty(),
                        new Statement.Body(
                                new Range(new Position(18, 2), new Position(1, 4)),
                                List.of(new Statement.AssignVariable(
                                        Range.sameLine(2, 9, 3),
                                        "r",
                                        new Expr.Mul(
                                                Range.sameLine(6, 9, 3),
                                                new Expr.VarCall(Range.sameLine(6, 7, 3), "r"),
                                                new Expr.VarCall(Range.sameLine(8, 9, 3), "i")
                                        )
                                ))
                        )
                )
        ));

        assertEquals(expected, Chromatynk.parseSource(source));
    }
}
