package fr.cyu.chromatynk.test.parsing;

import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.ast.Program;
import fr.cyu.chromatynk.ast.Statement;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.parsing.ParsingIterator;
import fr.cyu.chromatynk.parsing.StatementParser;
import fr.cyu.chromatynk.parsing.Token;
import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.Range;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static fr.cyu.chromatynk.test.parsing.ParserTest.assertParse;

public class StatementTestCase {

    @Test
    public void zeroArg() {
        //HIDE
        assertParse(
                new Statement.Hide(Range.sameLine(0, 4)),
                StatementParser.zeroArg(),
                ParsingIterator.of(new Token.Hide(Range.sameLine(0, 4)))
        );

        //SHOW
        assertParse(
                new Statement.Show(Range.sameLine(0, 4)),
                StatementParser.zeroArg(),
                ParsingIterator.of(new Token.Show(Range.sameLine(0, 4)))
        );
    }

    @Test
    public void oneArg() {
        //FWD 5
        assertParse(
                new Statement.Forward(Range.sameLine(0, 5), new Expr.LiteralInt(Range.sameLine(4, 5), 5)),
                StatementParser.oneArg(),
                ParsingIterator.of(
                        new Token.Fwd(Range.sameLine(0, 3)),
                        new Token.LiteralInt(Range.sameLine(4, 5), 5)
                )
        );
    }

    @Test
    public void twoArgs() {
        //MOV 5, 6
        assertParse(
                new Statement.Move(
                        Range.sameLine(0, 8),
                        new Expr.LiteralInt(Range.sameLine(4, 5), 5),
                        new Expr.LiteralInt(Range.sameLine(7, 8), 6)
                ),
                StatementParser.twoArgs(),
                ParsingIterator.of(
                        new Token.Mov(Range.sameLine(0, 3)),
                        new Token.LiteralInt(Range.sameLine(4, 5), 5),
                        new Token.Comma(Range.sameLine(5, 6)),
                        new Token.LiteralInt(Range.sameLine(7, 8), 6)
                )
        );
    }

    @Test
    public void threeArgs() {
        //COLOR 255, 255, 255
        assertParse(
                new Statement.ColorRGB(
                        Range.sameLine(0, 14),
                        new Expr.LiteralInt(Range.sameLine(6, 9), 255),
                        new Expr.LiteralInt(Range.sameLine(11, 14), 255),
                        new Expr.LiteralInt(Range.sameLine(16, 19), 255)
                ),
                StatementParser.threeArgs(),
                ParsingIterator.of(
                        new Token.Color(Range.sameLine(0, 5)),
                        new Token.LiteralInt(Range.sameLine(6, 9), 255),
                        new Token.Comma(Range.sameLine(9, 10)),
                        new Token.LiteralInt(Range.sameLine(11, 14), 255),
                        new Token.Comma(Range.sameLine(14, 15)),
                        new Token.LiteralInt(Range.sameLine(16, 19), 255)
                )
        );
    }

    @Test
    public void overloading() {
        //COLOR #FFF
        assertParse(
                new Statement.Color(
                        Range.sameLine(0, 10),
                        new Expr.LiteralColor(Range.sameLine(6, 10), 1, 1, 1, 1)
                ),
                StatementParser.instruction(),
                ParsingIterator.of(
                        new Token.Color(Range.sameLine(0, 5)),
                        new Token.LiteralColor(Range.sameLine(6, 10), "#FFF")
                )
        );

        //COLOR 255, 255, 255
        assertParse(
                new Statement.ColorRGB(
                        Range.sameLine(0, 14),
                        new Expr.LiteralInt(Range.sameLine(6, 9), 255),
                        new Expr.LiteralInt(Range.sameLine(11, 14), 255),
                        new Expr.LiteralInt(Range.sameLine(16, 19), 255)
                ),
                StatementParser.instruction(),
                ParsingIterator.of(
                        new Token.Color(Range.sameLine(0, 5)),
                        new Token.LiteralInt(Range.sameLine(6, 9), 255),
                        new Token.Comma(Range.sameLine(9, 10)),
                        new Token.LiteralInt(Range.sameLine(11, 14), 255),
                        new Token.Comma(Range.sameLine(14, 15)),
                        new Token.LiteralInt(Range.sameLine(16, 19), 255)
                )
        );
    }

    @Test
    public void variableDeclaration() {
        //BOOL x
        assertParse(
                new Statement.DeclareVariable(Range.sameLine(0, 6), Type.BOOLEAN, "x", Optional.empty()),
                StatementParser.variableDeclaration(),
                ParsingIterator.of(
                        new Token.Identifier(Range.sameLine(0, 4), "BOOL"),
                        new Token.Identifier(Range.sameLine(5, 6), "x")
                )
        );

        //BOOL x = true
        assertParse(
                new Statement.DeclareVariable(
                        Range.sameLine(0, 13),
                        Type.BOOLEAN,
                        "x",
                        Optional.of(new Expr.LiteralBool(Range.sameLine(9, 13), true))
                ),
                StatementParser.variableDeclaration(),
                ParsingIterator.of(
                        new Token.Identifier(Range.sameLine(0, 4), "BOOL"),
                        new Token.Identifier(Range.sameLine(5, 6), "x"),
                        new Token.Assign(Range.sameLine(7, 8)),
                        new Token.LiteralBool(Range.sameLine(9, 13), true)
                )
        );
    }

    @Test
    public void variableAssignment() {
        //x = true
        assertParse(
                new Statement.AssignVariable(
                        Range.sameLine(0, 8),
                        "x",
                        new Expr.LiteralBool(Range.sameLine(4, 8), true)
                ),
                StatementParser.variableAssignment(),
                ParsingIterator.of(
                        new Token.Identifier(Range.sameLine(0, 1), "x"),
                        new Token.Assign(Range.sameLine(2, 3)),
                        new Token.LiteralBool(Range.sameLine(4, 8), true)
                )
        );
    }

    @Test
    public void body() {
        //-> HIDE
        assertParse(
                new Statement.Body(
                        Range.sameLine(0,7),
                        List.of(new Statement.Hide(Range.sameLine(3,7)))
                ),
               StatementParser.body(),
                ParsingIterator.of(
                        new Token.Arrow(Range.sameLine(0,2)),
                        new Token.Hide(Range.sameLine(3,7))
                )
        );

        /*
        {
          HIDE
        }
         */
        assertParse(
                new Statement.Body(
                        new Range(new Position(0,0), new Position(1, 2)),
                        List.of(new Statement.Hide(Range.sameLine(2,6, 1)))
                ),
                StatementParser.body(),
                ParsingIterator.of(
                        new Token.BraceOpen(Range.sameLine(0,1)),
                        new Token.Hide(Range.sameLine(2,6, 1)),
                        new Token.BraceClosed(Range.sameLine(0,1, 2))
                )
        );
    }

    @Test
    public void whileLoop() {
        /*
        WHILE true {
          HIDE
        }
         */
        assertParse(
                new Statement.While(
                        new Range(new Position(0, 0), new Position(1, 2)),
                        new Expr.LiteralBool(Range.sameLine(6, 10), true),
                        new Statement.Body(
                                new Range(new Position(11, 0), new Position(1, 2)),
                                List.of(new Statement.Hide(Range.sameLine(2, 6, 1)))
                        )
                ),
                StatementParser.whileLoop(),
                ParsingIterator.of(
                        new Token.While(Range.sameLine(0, 5)),
                        new Token.LiteralBool(Range.sameLine(6, 10), true),
                        new Token.BraceOpen(Range.sameLine(11, 12)),
                        new Token.Hide(Range.sameLine(2, 6, 1)),
                        new Token.BraceClosed(Range.sameLine(0, 1, 2))
                )
        );
    }

    @Test
    public void forLoop() {
        /*
        FOR i FROM 0 TO 10 {
          HIDE
        }
         */
        assertParse(
                new Statement.For(
                        new Range(new Position(0, 0), new Position(1, 2)),
                        "i",
                        Optional.of(new Expr.LiteralInt(Range.sameLine(11, 12), 0)),
                        new Expr.LiteralInt(Range.sameLine(16, 18), 10),
                        Optional.empty(),
                        new Statement.Body(
                                new Range(new Position(19, 0), new Position(1, 2)),
                                List.of(new Statement.Hide(Range.sameLine(2, 6, 1)))
                        )
                ),
                StatementParser.forLoop(),
                ParsingIterator.of(
                        new Token.For(Range.sameLine(0, 3)),
                        new Token.Identifier(Range.sameLine(4, 5), "i"),
                        new Token.From(Range.sameLine(6, 10)),
                        new Token.LiteralInt(Range.sameLine(11, 12), 0),
                        new Token.To(Range.sameLine(13, 15)),
                        new Token.LiteralInt(Range.sameLine(16, 18), 10),
                        new Token.BraceOpen(Range.sameLine(19, 20)),
                        new Token.Hide(Range.sameLine(2, 6, 1)),
                        new Token.BraceClosed(Range.sameLine(0, 1, 2))
                )
        );

        /*
        FOR i FROM 0 TO 10 STEP 2 {
          HIDE
        }
         */
        assertParse(
                new Statement.For(
                        new Range(new Position(0, 0), new Position(1, 2)),
                        "i",
                        Optional.of(new Expr.LiteralInt(Range.sameLine(11, 12), 0)),
                        new Expr.LiteralInt(Range.sameLine(16, 18), 10),
                        Optional.of(new Expr.LiteralInt(Range.sameLine(24, 25), 2)),
                        new Statement.Body(
                                new Range(new Position(26, 0), new Position(1, 2)),
                                List.of(new Statement.Hide(Range.sameLine(2, 6, 1)))
                        )
                ),
                StatementParser.forLoop(),
                ParsingIterator.of(
                        new Token.For(Range.sameLine(0, 3)),
                        new Token.Identifier(Range.sameLine(4, 5), "i"),
                        new Token.From(Range.sameLine(6, 10)),
                        new Token.LiteralInt(Range.sameLine(11, 12), 0),
                        new Token.To(Range.sameLine(13, 15)),
                        new Token.LiteralInt(Range.sameLine(16, 18), 10),
                        new Token.Step(Range.sameLine(19, 23)),
                        new Token.LiteralInt(Range.sameLine(24, 25), 2),
                        new Token.BraceOpen(Range.sameLine(26, 27)),
                        new Token.Hide(Range.sameLine(2, 6, 1)),
                        new Token.BraceClosed(Range.sameLine(0, 1, 2))
                )
        );
    }

    @Test
    public void ifCondition() {
        /*
        IF true {
          HIDE
        }
         */
        assertParse(
                new Statement.If(
                        new Range(new Position(0, 0), new Position(1, 2)),
                        new Expr.LiteralBool(Range.sameLine(3, 7), true),
                        new Statement.Body(
                                new Range(new Position(8, 0), new Position(1, 2)),
                                List.of(new Statement.Hide(Range.sameLine(2, 6, 1)))
                        ),
                        Optional.empty()
                ),
                StatementParser.ifCondition(),
                ParsingIterator.of(
                        new Token.If(Range.sameLine(0, 2)),
                        new Token.LiteralBool(Range.sameLine(3, 7), true),
                        new Token.BraceOpen(Range.sameLine(8, 9)),
                        new Token.Hide(Range.sameLine(2, 6, 1)),
                        new Token.BraceClosed(Range.sameLine(0, 1, 2))
                )
        );

        /*
        IF true {
          HIDE
        } ELSE {
          SHOW
        }
         */
        assertParse(
                new Statement.If(
                        new Range(new Position(0, 0), new Position(1, 4)),
                        new Expr.LiteralBool(Range.sameLine(3, 7), true),
                        new Statement.Body(
                                new Range(new Position(8, 0), new Position(1, 2)),
                                List.of(new Statement.Hide(Range.sameLine(2, 6, 1)))
                        ),
                        Optional.of(new Statement.Body(
                                new Range(new Position(7, 2), new Position(1, 4)),
                                List.of(new Statement.Show(Range.sameLine(2, 6, 3)))
                        ))
                ),
                StatementParser.ifCondition(),
                ParsingIterator.of(
                        new Token.If(Range.sameLine(0, 2)),
                        new Token.LiteralBool(Range.sameLine(3, 7), true),
                        new Token.BraceOpen(Range.sameLine(8, 9)),
                        new Token.Hide(Range.sameLine(2, 6, 1)),
                        new Token.BraceClosed(Range.sameLine(0, 1, 2)),
                        new Token.Else(Range.sameLine(2, 6, 2)),
                        new Token.BraceOpen(Range.sameLine(7, 8, 2)),
                        new Token.Show(Range.sameLine(2, 6, 3)),
                        new Token.BraceClosed(Range.sameLine(0, 1, 4))
                )
        );
    }

    @Test
    public void mimic() {
        /*
        MIMIC "cursor" {
          FWD 5
        }
         */
        assertParse(
                new Statement.Mimic(
                        new Range(new Position(0, 0), new Position(1, 2)),
                        new Expr.LiteralString(Range.sameLine(6, 14), "cursor"),
                        new Statement.Body(
                                new Range(new Position(15, 0), new Position(1, 2)),
                                List.of(
                                        new Statement.Forward(
                                                Range.sameLine(2, 7, 1),
                                                new Expr.LiteralInt(Range.sameLine(6, 7, 1), 5)
                                        )
                                )
                        )
                ),
                StatementParser.mimic(),
                ParsingIterator.of(
                        new Token.Mimic(Range.sameLine(0, 5)),
                        new Token.LiteralString(Range.sameLine(6, 14), "cursor"),
                        new Token.BraceOpen(Range.sameLine(15, 16)),
                        new Token.Fwd(Range.sameLine(2, 5, 1)),
                        new Token.LiteralInt(Range.sameLine(6, 7, 1), 5),
                        new Token.BraceClosed(Range.sameLine(0, 1, 2))
                )
        );
    }

    @Test
    public void mirrorCentral() {
        /*
        MIRROR 0, 0 {
          FWD 5
        }
         */
        assertParse(
                new Statement.MirrorCentral(
                        new Range(new Position(0, 0), new Position(1, 2)),
                        new Expr.LiteralInt(Range.sameLine(7, 8), 0),
                        new Expr.LiteralInt(Range.sameLine(10, 11), 0),
                        new Statement.Body(
                                new Range(new Position(12, 0), new Position(1, 2)),
                                List.of(
                                        new Statement.Forward(
                                                Range.sameLine(2, 7, 1),
                                                new Expr.LiteralInt(Range.sameLine(6, 7, 1), 5)
                                        )
                                )
                        )
                ),
                StatementParser.mirrorCentral(),
                ParsingIterator.of(
                        new Token.Mirror(Range.sameLine(0, 6)),
                        new Token.LiteralInt(Range.sameLine(7, 8), 0),
                        new Token.Comma(Range.sameLine(8, 9)),
                        new Token.LiteralInt(Range.sameLine(10, 11), 0),
                        new Token.BraceOpen(Range.sameLine(12, 13)),
                        new Token.Fwd(Range.sameLine(2, 5, 1)),
                        new Token.LiteralInt(Range.sameLine(6, 7, 1), 5),
                        new Token.BraceClosed(Range.sameLine(0, 1, 2))
                )
        );
    }

    @Test
    public void mirrorAxial() {
        /*
        MIRROR 0, 0, 5, 5 {
          FWD 5
        }
         */
        assertParse(
                new Statement.MirrorAxial(
                        new Range(new Position(0, 0), new Position(1, 2)),
                        new Expr.LiteralInt(Range.sameLine(7, 8), 0),
                        new Expr.LiteralInt(Range.sameLine(10, 11), 0),
                        new Expr.LiteralInt(Range.sameLine(13, 14), 5),
                        new Expr.LiteralInt(Range.sameLine(16, 17), 5),
                        new Statement.Body(
                                new Range(new Position(18, 0), new Position(1, 2)),
                                List.of(
                                        new Statement.Forward(
                                                Range.sameLine(2, 7, 1),
                                                new Expr.LiteralInt(Range.sameLine(6, 7, 1), 5)
                                        )
                                )
                        )
                ),
                StatementParser.mirrorAxial(),
                ParsingIterator.of(
                        new Token.Mirror(Range.sameLine(0, 6)),
                        new Token.LiteralInt(Range.sameLine(7, 8), 0),
                        new Token.Comma(Range.sameLine(8, 9)),
                        new Token.LiteralInt(Range.sameLine(10, 11), 0),
                        new Token.Comma(Range.sameLine(11, 12)),
                        new Token.LiteralInt(Range.sameLine(13, 14), 5),
                        new Token.Comma(Range.sameLine(14, 15)),
                        new Token.LiteralInt(Range.sameLine(16, 17), 5),
                        new Token.BraceOpen(Range.sameLine(18, 19)),
                        new Token.Fwd(Range.sameLine(2, 5, 1)),
                        new Token.LiteralInt(Range.sameLine(6, 7, 1), 5),
                        new Token.BraceClosed(Range.sameLine(0, 1, 2))
                )
        );
    }

    @Test
    public void program() {
        /*
        INT n = 5
        INT r = 1
        FOR i FROM 2 TO n {
          r = r*i
        }
         */

        assertParse(
                new Program(List.of(
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
                                Optional.of(new Expr.LiteralInt(Range.sameLine(11, 12, 2), 2)),
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
                )),
                StatementParser.program(),
                ParsingIterator.of(
                        //INT n = 5
                        new Token.Identifier(Range.sameLine(0, 3), "INT"),
                        new Token.Identifier(Range.sameLine(4, 5), "n"),
                        new Token.Assign(Range.sameLine(6, 7)),
                        new Token.LiteralInt(Range.sameLine(8, 9), 5),

                        //INT r = 1
                        new Token.Identifier(Range.sameLine(0, 3, 1), "INT"),
                        new Token.Identifier(Range.sameLine(4, 5, 1), "r"),
                        new Token.Assign(Range.sameLine(6, 7, 1)),
                        new Token.LiteralInt(Range.sameLine(8, 9, 1), 1),

                        //FOR i FROM 2 TO n {
                        new Token.For(Range.sameLine(0, 3, 2)),
                        new Token.Identifier(Range.sameLine(4, 5, 2), "i"),
                        new Token.From(Range.sameLine(6, 10, 2)),
                        new Token.LiteralInt(Range.sameLine(11, 12, 2), 2),
                        new Token.To(Range.sameLine(13, 15, 2)),
                        new Token.Identifier(Range.sameLine(16, 17, 2), "n"),
                        new Token.BraceOpen(Range.sameLine(18, 19, 2)),

                        //  r = r*i
                        new Token.Identifier(Range.sameLine(2, 3, 3), "r"),
                        new Token.Assign(Range.sameLine(4, 5, 3)),
                        new Token.Identifier(Range.sameLine(6, 7, 3), "r"),
                        new Token.Mul(Range.sameLine(7, 8, 3)),
                        new Token.Identifier(Range.sameLine(8, 9, 3), "i"),

                        //}
                        new Token.BraceClosed(Range.sameLine(0, 1, 4)),

                        //END
                        new Token.EndOfFile(new Position(1, 4))
                )
        );
    }
}
