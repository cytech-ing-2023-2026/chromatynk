package fr.cyu.chromatynk.ast;

import java.util.List;

/**
 * The statements of the language.
 */
public sealed interface Statement {

    /**
     * Move forward on the given distance.
     *
     * @param distance the distance to travel
     */
    record Forward(Expr distance) implements Statement {}

    /**
     * Move backward on the given distance.
     *
     * @param distance the distance to travel
     */
    record Backward(Expr distance) implements Statement {}

    /**
     * A for loop, repeating while the iterator is less than `to`.
     *
     * <pre>
     *     FOR iterator FROM from TO to STEP step {
     *       body
     *     }
     * </pre>
     *
     * @param iterator the name of the iterator
     * @param from     the initial thickness of the iterator
     * @param to       the upper bound of the iterator
     * @param step     the step to increment the iterator
     * @param body     the statement to execute
     */
    record For(String iterator, Expr from, Expr to, Expr step, Statement body) implements Statement {}

    /**
     * A Turn {@code angle}.
     *
     * @param angle the thickness for the rotation of the turn
     */
    record Turn(Expr angle) implements Statement {}

    /**
     * A While loop {@code condition, body}.
     *
     * @param condition the condition during which the while continues to loop
     * @param body      the body of the loop executed as long as the condition is true
     */
    record While(Expr condition, Statement body) implements Statement {}

    /**
     * Teleport the cursor to the given position.
     *
     * @param x the new X coordinate of the cursor
     * @param y the new Y coordinate of the cursor
     */
    record Pos(Expr x, Expr y) implements Statement {}

    /**
     * A block containing zero or more statements.
     *
     * @param statements the statements
     */
    record Block(List<Statement> statements) implements Statement {}

    /**
     * Move the current cursor relatively
     *
     * @param distanceX the distance to translate on the X axis
     * @param distanceY the distance to translate on the Y axis
     */
    record Move(Expr distanceX, Expr distanceY) implements Statement {}

    /**
     * Hide the given cursor.
     *
     * @param cursor the id of the cursor to move
     */
    record Hide(Expr cursor) implements Statement {}

    /**
     * Show the given cursor.
     *
     * @param cursor the id of the cursor to move
     */
    record Show(Expr cursor) implements Statement {}

    /**
     * Set the opacity of the cursor.
     *
     * @param opacity the opacity to apply when drawing
     */
    record Press(Expr opacity) implements Statement {}

    /**
     * Set the thickness of the cursor.
     *
     * @param thickness the thickness of the cursor
     */
    record Thick(int thickness) implements Statement {}

    /**
     * Make the current cursor look at the given one.
     *
     * @param cursor
     */
    record LookAtCursor(Expr cursor) implements Statement {}

    /**
     * Make the current cursor look at the given position.
     *
     * @param targetX the X coordinate of the point to look at
     * @param targetY the Y coordinate of the point to look at
     */
    record LookAtPos(Expr targetX, Expr targetY) implements Statement {}

    /**
     * Create a new cursor with the given id.
     *
     * @param id the id of the new cursor
     */
    record CreateCursor(String id) implements Statement {}

    /**
     * Select a cursor.
     *
     * @param id the id of the cursor to select
     */
    record SelectCursor(String id) implements Statement {}

    /**
     * Remove a cursor.
     *
     * @param id the id of the cursor to remove
     */
    record RemoveCursor(String id) implements Statement {}

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
     * @param condition
     * @param ifTrue
     * @param ifFalse
     */
    record If(Expr condition, Statement ifTrue, Statement ifFalse) implements Statement {}

    /**
     * Create a new cursor mimicking the given one.
     *
     * @param mimicked the mimicked cursor
     */
    record Mimic(String mimicked) implements Statement {}

    /**
     * Duplicate the current cursor by making a central symmetry.
     *
     * @param centerX the X coordinate of the symmetry center
     * @param centerY the Y coordinate of the symmetry center
     */
    record MirrorCentral(Expr centerX, Expr centerY) implements Statement {}

    /**
     * Duplicate the current cursor by making an axial symmetry.
     *
     * @param axisStartX the X coordinate of the start of the axis
     * @param axisStartY the Y coordinate of the start of the axis
     * @param axisEndX   the X coordinate of the end of the axis
     * @param axisEndY   the Y coordinate of the end of the axis
     */
    record MirrorAxial(Expr axisStartX, Expr axisStartY, Expr axisEndX, Expr axisEndY) implements Statement {}
}
