package fr.cyu.chromatynk.ast;

import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.Range;

import java.util.List;

/**
 * The statements of the language.
 */
public sealed interface Statement {

    /**
     * Get the range of this statement.
     * 
     * @return the starting and ending position of this statement
     */
    Range range();

    /**
     * Move forward on the given distance.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param distance the distance to travel
     */
    record Forward(Range range, Expr distance) implements Statement {}

    /**
     * Move backward on the given distance.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param distance the distance to travel
     */
    record Backward(Range range, Expr distance) implements Statement {}

    /**
     * A for loop, repeating while the iterator is less than `to`.
     *
     * <pre>
     *     FOR iterator FROM from TO to STEP step {
     *       body
     *     }
     * </pre>
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param iterator the name of the iterator
     * @param from     the initial thickness of the iterator
     * @param to       the upper bound of the iterator
     * @param step     the step to increment the iterator
     * @param body     the statement to execute
     */
    record For(Range range, String iterator, Expr from, Expr to, Expr step, Statement body) implements Statement {}

    /**
     * A Turn {@code angle}.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param angle the thickness for the rotation of the turn
     */
    record Turn(Range range, Expr angle) implements Statement {}

    /**
     * A While loop {@code condition, body}.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param condition the condition during which the while continues to loop
     * @param body      the body of the loop executed as long as the condition is true
     */
    record While(Range range, Expr condition, Statement body) implements Statement {}

    /**
     * Teleport the cursor to the given position.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param x the new X coordinate of the cursor
     * @param y the new Y coordinate of the cursor
     */
    record Pos(Range range, Expr x, Expr y) implements Statement {}

    /**
     * A block containing zero or more statements.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param statements the statements
     */
    record Block(Range range, List<Statement> statements) implements Statement {}

    /**
     * Move the current cursor relatively
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param distanceX the distance to translate on the X axis
     * @param distanceY the distance to translate on the Y axis
     */
    record Move(Range range, Expr distanceX, Expr distanceY) implements Statement {}

    /**
     * Hide the given cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param cursor the id of the cursor to move
     */
    record Hide(Range range, Expr cursor) implements Statement {}

    /**
     * Show the given cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param cursor the id of the cursor to move
     */
    record Show(Range range, Expr cursor) implements Statement {}

    /**
     * Set the opacity of the cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param opacity the opacity to apply when drawing
     */
    record Press(Range range, Expr opacity) implements Statement {}

    /**
     * Set the thickness of the cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param thickness the thickness of the cursor
     */
    record Thick(Range range, int thickness) implements Statement {}

    /**
     * Make the current cursor look at the given one.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param cursor
     */
    record LookAtCursor(Range range, Expr cursor) implements Statement {}

    /**
     * Make the current cursor look at the given position.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param targetX the X coordinate of the point to look at
     * @param targetY the Y coordinate of the point to look at
     */
    record LookAtPos(Range range, Expr targetX, Expr targetY) implements Statement {}

    /**
     * Create a new cursor with the given id.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param id the id of the new cursor
     */
    record CreateCursor(Range range, String id) implements Statement {}

    /**
     * Select a cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param id the id of the cursor to select
     */
    record SelectCursor(Range range, String id) implements Statement {}

    /**
     * Remove a cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param id the id of the cursor to remove
     */
    record RemoveCursor(Range range, String id) implements Statement {}

    /**
     * An if condition.
     *
     * <pre>
     *     IF condition {
     *         ifTrue
     *     } ELSE {
     *         ifFalse
     *     }
     * </pre>
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param condition
     * @param ifTrue
     * @param ifFalse
     */
    record If(Range range, Expr condition, Statement ifTrue, Statement ifFalse) implements Statement {}

    /**
     * Create a new cursor mimicking the given one.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param mimicked the mimicked cursor
     */
    record Mimic(Range range, String mimicked) implements Statement {}

    /**
     * Duplicate the current cursor by making a central symmetry.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param centerX the X coordinate of the symmetry center
     * @param centerY the Y coordinate of the symmetry center
     */
    record MirrorCentral(Range range, Expr centerX, Expr centerY) implements Statement {}

    /**
     * Duplicate the current cursor by making an axial symmetry.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param axisStartX the X coordinate of the start of the axis
     * @param axisStartY the Y coordinate of the start of the axis
     * @param axisEndX   the X coordinate of the end of the axis
     * @param axisEndY   the Y coordinate of the end of the axis
     */
    record MirrorAxial(Range range, Expr axisStartX, Expr axisStartY, Expr axisEndX, Expr axisEndY) implements Statement {}
}
