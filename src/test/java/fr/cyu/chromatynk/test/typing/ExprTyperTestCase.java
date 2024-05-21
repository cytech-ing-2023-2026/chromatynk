package fr.cyu.chromatynk.test.typing;

import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.typing.TypeMismatchException;
import fr.cyu.chromatynk.typing.Typer;
import fr.cyu.chromatynk.typing.TypingContext;
import fr.cyu.chromatynk.typing.TypingException;
import fr.cyu.chromatynk.util.Range;
import org.junit.jupiter.api.Test;

import static fr.cyu.chromatynk.typing.Typer.getType;
import static java.awt.Color.green;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExprTyperTestCase {

    @Test
    public void literal() throws TypingException {
        TypingContext context = new TypingContext();
        assertEquals(Type.BOOLEAN, getType(new Expr.LiteralBool(Range.sameLine(0, 0), true), context));
        assertEquals(Type.STRING, getType(new Expr.LiteralString(Range.sameLine(0, 0), "a"), context));
        assertEquals(Type.INT, getType(new Expr.LiteralInt(Range.sameLine(0, 0), 2), context));
        assertEquals(Type.FLOAT, getType(new Expr.LiteralFloat(Range.sameLine(0, 0), 2.5), context));
        assertEquals(Type.COLOR, getType(new Expr.LiteralColor(Range.sameLine(0, 0), 0, 0, 0, 0), context));
    }

    @Test
    public void percent() throws TypingException {
        TypingContext context = new TypingContext();
        assertEquals(Type.PERCENTAGE, getType(new Expr.Percent(Range.sameLine(0, 0), new Expr.LiteralInt(Range.sameLine(0, 0), 5)), context));
    }

    @Test
    public void negation() throws TypingException {
        TypingContext context = new TypingContext();
        assertEquals(Type.INT, getType(new Expr.Negation(Range.sameLine(0, 0),new Expr.LiteralInt(Range.sameLine(0, 0), 3)), context));
        assertEquals(Type.FLOAT, getType(new Expr.Negation(Range.sameLine(0, 0),new Expr.LiteralFloat(Range.sameLine(0, 0), 3)), context));
    }

    @Test
    public void Add() throws TypingException {
        TypingContext context = new TypingContext();
        assertEquals(Type.INT, getType(new Expr.Add(Range.sameLine(0, 0),new Expr.LiteralInt(Range.sameLine(0, 0), 2), new Expr.LiteralInt(Range.sameLine(0,0), 2)), context));
        assertEquals(Type.FLOAT, getType(new Expr.Add(Range.sameLine(0, 0),new Expr.LiteralFloat(Range.sameLine(0, 0), 2), new Expr.LiteralInt(Range.sameLine(0,0), 3)), context));
        assertEquals(Type.FLOAT, getType(new Expr.Add(Range.sameLine(0, 0),new Expr.LiteralFloat(Range.sameLine(0, 0), 2), new Expr.LiteralFloat(Range.sameLine(0,0), 3)), context));
        assertEquals(Type.COLOR, getType(new Expr.Add(Range.sameLine(0, 0),new Expr.LiteralColor(Range.sameLine(0, 0), 0,0,0,0), new Expr.LiteralColor(Range.sameLine(0,0), 3,3,4,1)), context));
        assertThrows(TypeMismatchException.class, () -> getType(new Expr.Add(Range.sameLine(0, 0),new Expr.LiteralFloat(Range.sameLine(0, 0), 0), new Expr.LiteralColor(Range.sameLine(0,0), 3,3,4,1)), context));
    }

    @Test
    public void Sub() throws TypingException {
        TypingContext context = new TypingContext();
        assertEquals(Type.INT, getType(new Expr.Sub(Range.sameLine(0, 0),new Expr.LiteralInt(Range.sameLine(0, 0), 2), new Expr.LiteralInt(Range.sameLine(0,0), 3)), context));
        assertEquals(Type.FLOAT, getType(new Expr.Sub(Range.sameLine(0, 0),new Expr.LiteralFloat(Range.sameLine(0, 0), 2), new Expr.LiteralFloat(Range.sameLine(0,0),2)), context));
    }
}
