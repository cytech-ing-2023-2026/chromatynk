package fr.cyu.chromatynk.test.typing;

import fr.cyu.chromatynk.ast.Expr;
import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.typing.Typer;
import fr.cyu.chromatynk.typing.TypingContext;
import fr.cyu.chromatynk.typing.TypingException;
import fr.cyu.chromatynk.util.Range;
import org.junit.jupiter.api.Test;

import static java.awt.Color.green;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExprTyperTestCase {

    @Test
    public void literal() throws TypingException {
        TypingContext context = new TypingContext();
        assertEquals(Type.BOOLEAN, Typer.getType(new Expr.LiteralBool(Range.sameLine(0, 0), true), context));
        assertEquals(Type.STRING, Typer.getType(new Expr.LiteralString(Range.sameLine(0, 0), "a"), context));
        assertEquals(Type.INT, Typer.getType(new Expr.LiteralInt(Range.sameLine(0, 0), 2), context));
        assertEquals(Type.FLOAT, Typer.getType(new Expr.LiteralFloat(Range.sameLine(0, 0), 2.5), context));
        assertEquals(Type.COLOR, Typer.getType(new Expr.LiteralColor(Range.sameLine(0, 0), green), context));
    }
}
