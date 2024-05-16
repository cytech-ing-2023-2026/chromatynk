package fr.cyu.chromatynk.test.eval;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.draw.Color;
import fr.cyu.chromatynk.draw.CursorId;
import fr.cyu.chromatynk.draw.TangibleCursor;
import fr.cyu.chromatynk.eval.*;
import fr.cyu.chromatynk.util.Range;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ScopeTestCase {

    private Scope scopeWithParent() {
        Scope parent = new Scope(
                null,
                new HashMap<>(Map.of("y", new Variable(Type.BOOLEAN, new Value.Bool(true)))),
                new HashMap<>(Map.of(new CursorId.Str("parent"), new TangibleCursor(0, 0)))
        );

        return new Scope(
                parent,
                new HashMap<>(Map.of("x", new Variable(Type.INT, new Value.Int(5)))),
                new HashMap<>(Map.of(new CursorId.Str("cursor"), new TangibleCursor(10, 5)))
        );
    }

    @Test
    public void variableDeclaration() {
        Scope scope = new Scope(null, new HashMap<>(), new HashMap<>());
        assertFalse(scope.containsVariable("z"));
        assertThrows(TypeMismatchException.class, () -> scope.declareVariable("z", new Variable(Type.INT, new Value.Str("hey")), Range.sameLine(0, 0)));
        assertDoesNotThrow(() -> scope.declareVariable("z", new Variable(Type.INT, new Value.Int(5)), Range.sameLine(0, 0)));
        assertThrows(VariableAlreadyExistsException.class, () -> scope.declareVariable("z", new Variable(Type.INT, new Value.Int(5)), Range.sameLine(0, 0)));
        assertEquals(new Value.Int(5), scope.getValue("z").get());
    }

    @Test
    public void variableAssignation() {
        Scope scope = scopeWithParent();

        assertThrows(TypeMismatchException.class, () -> scope.setValue("x", new Value.Bool(false), Range.sameLine(0, 0)));
        assertDoesNotThrow(() -> scope.setValue("x", new Value.Int(10), Range.sameLine(0, 0)));
        assertEquals(new Value.Int(10), scope.getValue("x").get());

        assertDoesNotThrow(() -> scope.setValue("y", new Value.Bool(false), Range.sameLine(0, 0)));
        assertEquals(new Value.Bool(false), scope.getValue("y").get());

        assertThrows(MissingVariableException.class, () -> scope.setValue("z", new Value.Int(5), Range.sameLine(0, 0)));
    }

    @Test
    public void cursorDeclaration() {
        Scope scope = new Scope(null, new HashMap<>(), new HashMap<>());
        assertFalse(scope.containsCursor(new CursorId.Str("cursor")));
        assertDoesNotThrow(() -> scope.declareCursor(new CursorId.Str("cursor"), new TangibleCursor(0, 0), Range.sameLine(0, 0)));
        assertEquals(0, scope.getCursor(new CursorId.Str("cursor")).get().getX());
        assertEquals(0, scope.getCursor(new CursorId.Str("cursor")).get().getY());
        assertThrows(CursorAlreadyExistsException.class, () -> scope.declareCursor(new CursorId.Str("cursor"), new TangibleCursor(0, 0), Range.sameLine(0, 0)));
    }

    @Test
    public void parent() {
        Scope scope = scopeWithParent().getParent().get();

        assertEquals(new Value.Bool(true), scope.getValue("y").get());
    }
}
