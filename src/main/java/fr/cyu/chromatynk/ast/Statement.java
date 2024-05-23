package fr.cyu.chromatynk.ast;

import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.Range;

import java.util.List;
import java.util.Optional;

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
     * A body of a control structure containing zero or more statements.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param statements the statements of this body
     */
    record Body(Range range, List<Statement> statements) implements Statement {}

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
     * @param range    the starting and ending {@link Position} of this statement
     * @param iterator the name of the iterator
     * @param from     the initial thickness of the iterator
     * @param to       the upper bound of the iterator
     * @param step     the step to increment the iterator
     * @param body     the statements to execute
     */
    record For(Range range, String iterator, Optional<Expr> from, Expr to, Optional<Expr> step, Body body) implements Statement {}

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
    record While(Range range, Expr condition, Body body) implements Statement {}

    /**
     * Teleport the cursor to the given position.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param x the new X coordinate of the cursor
     * @param y the new Y coordinate of the cursor
     */
    record Pos(Range range, Expr x, Expr y) implements Statement {}

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
     */
    record Hide(Range range) implements Statement {}

    /**
     * Show the given cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     */
    record Show(Range range) implements Statement {}

    /**
     * Set the opacity of the cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param opacity the opacity to apply when drawing
     */
    record Press(Range range, Expr opacity) implements Statement {}

    /**
     * Set the color of the cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param color the color to use when drawing
     */
    record Color(Range range, Expr color) implements Statement {}

    /**
     * Set the color of the cursor. Takes 3 arguments: red, green, blue.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param red the red color component
     * @param green the green color component
     * @param blue the blue color component
     */
    record ColorRGB(Range range, Expr red, Expr green, Expr blue) implements Statement {}

    /**
     * Set the thickness of the cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param thickness the thickness of the cursor
     */
    record Thick(Range range, Expr thickness) implements Statement {}

    /**
     * Make the current cursor look at the given one.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param cursor the cursor
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
    record CreateCursor(Range range, Expr id) implements Statement {}

    /**
     * Select a cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param id the id of the cursor to select
     */
    record SelectCursor(Range range, Expr id) implements Statement {}

    /**
     * Remove a cursor.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param id the id of the cursor to remove
     */
    record RemoveCursor(Range range, Expr id) implements Statement {}

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
     * @param condition the condition used to choose between `ifTrue` or `ifFalse`
     * @param ifTrue the body to execute if the condition is true
     * @param ifFalse the optional body to execute if the condition is false aka the "else" body
     */
    record If(Range range, Expr condition, Body ifTrue, Optional<Body> ifFalse) implements Statement {}

    /**
     * Create a new cursor mimicking the given one.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param mimicked the mimicked cursor
     * @param body the statements to execute while mimicking the given cursor
     */
    record Mimic(Range range, Expr mimicked, Body body) implements Statement {}

    /**
     * Duplicate the current cursor by making a central symmetry.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param centerX the X coordinate of the symmetry center
     * @param centerY the Y coordinate of the symmetry center
     * @param body the statements to execute while mirroring the given cursor
     */
    record MirrorCentral(Range range, Expr centerX, Expr centerY, Body body) implements Statement {}

    /**
     * Duplicate the current cursor by making an axial symmetry.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param axisStartX the X coordinate of the start of the axis
     * @param axisStartY the Y coordinate of the start of the axis
     * @param axisEndX   the X coordinate of the end of the axis
     * @param axisEndY   the Y coordinate of the end of the axis
     * @param body the statements to execute while mirroring the given cursor
     */
    record MirrorAxial(Range range, Expr axisStartX, Expr axisStartY, Expr axisEndX, Expr axisEndY, Body body) implements Statement {}

    /**
     * Declare a new variable.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param type the type of the new variable
     * @param name the name of the new variable
     * @param value the optional value assigned at declaration
     */
    record DeclareVariable(Range range, Type type, String name, Optional<Expr> value) implements Statement {}

    /**
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param name the name of the variable to mutate
     * @param value the value to assign
     */
    record AssignVariable(Range range, String name, Expr value) implements Statement {}

    /**
     * Delete a variable.
     *
     * @param range the starting and ending {@link Position} of this statement
     * @param name the name of the variable to delete
     */
    record DeleteVariable(Range range, String name) implements Statement {}

}
