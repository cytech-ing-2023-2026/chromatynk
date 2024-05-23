package fr.cyu.chromatynk.bytecode;

import fr.cyu.chromatynk.ast.Type;
import fr.cyu.chromatynk.eval.Value;
import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.Range;

/**
 * An atomic stack-based instruction.
 */
public sealed interface Bytecode {

    /**
     * Get the range of this bytecode instruction.
     *
     * @return the starting and ending position of this bytecode instruction
     */
    Range range();

    /**
     * Check if this instruction is effectful.
     *
     * @return `true` if this instruction produces an effect.
     */
    default boolean isEffectful() {
        return this instanceof Effectful;
    }

    //Control

    /**
     * An expression that has an effect (mutation, drawing...).
     */
    sealed interface Effectful extends Bytecode {}

    /**
     * Push a value to the stack.
     *
     * @param range the starting and ending {@link Position} of this instruction
     * @param value the pushed value
     */
    record Push(Range range, Value value) implements Bytecode {}

    /**
     * Push the value of a variable to the stack.
     *
     * @param range the starting and ending {@link Position} of this instruction
     * @param name the name of the variable to load
     */
    record Load(Range range, String name) implements Bytecode {}

    /**
     * Pop a value and store it in the given variable.
     *
     * @param range the starting and ending {@link Position} of this instruction
     * @param name the name of the variable to store the value into
     */
    record Store(Range range, String name) implements Effectful {}

    /**
     * Declare a variable and pop a value from the stack to assign to it.
     *
     * @param range the starting and ending {@link Position} of this instruction
     * @param name the name of the declared variable
     */
    record Declare(Range range, Type type, String name) implements Effectful {}

    /**
     * Delete a variable.
     *
     * @param range the starting and ending {@link Position} of this instruction
     * @param name the name of the variable to delete
     */
    record Delete(Range range, String name) implements Effectful {}

    /**
     * Jump to the given address.
     *
     * @param range the starting and ending {@link Position} of this instruction
     * @param address the address to go to
     */
    record GoTo(Range range, int address) implements Bytecode {}

    /**
     * Pop a value from the stack then jump to an address if the popped value is `false`.
     *
     * @param range the starting and ending {@link Position} of this instruction
     * @param addressFalse the address to jump to if the popped value is `false`
     */
    record GoToIfFalse(Range range, int addressFalse) implements Bytecode {}

    /**
     * The end of the scope.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record End(Range range) implements Bytecode {}

    /**
     * Create a new enclosed scope.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record NewScope(Range range) implements Bytecode {}

    /**
     * Use the parent scope and discard the current one.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record ExitScope(Range range) implements Bytecode {}

    //Operations

    /**
     * Pop a value from the stack then push it as a percentage.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Percent(Range range) implements Bytecode {}

    /**
     * Pop a value from the stack then push the opposite.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Negation(Range range) implements Bytecode {}

    /**
     * Pop two values from the stack then push sum.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Add(Range range) implements Bytecode {}

    /**
     * Pop two values from the stack then push difference.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Sub(Range range) implements Bytecode {}

    /**
     * Pop two values from the stack then push multiplication.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Mul(Range range) implements Bytecode {}

    /**
     * Pop two values from the stack then push division.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Div(Range range) implements Bytecode {}

    /**
     * Pop a boolean from the stack then push opposite.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Not(Range range) implements Bytecode {}

    /**
     * Pop two booleans from the stack then push one or the other.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Or(Range range) implements Bytecode {}

    /**
     * Pop two booleans from the stack then push together.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record And(Range range) implements Bytecode {}

    /**
     * Pop two values from the stack then push `true` if the popped values are equal, `false` otherwise.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Equal(Range range) implements Bytecode {}

    /**
     * Pop two values from the stack then push `true` if the popped values are not equal, `false` otherwise.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record NotEqual(Range range) implements Bytecode {}

    /**
     * Pop two values from the stack then push `true` if the first popped value is greater than the second, `false` otherwise.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Greater(Range range) implements Bytecode {}

    /**
     * Pop two values from the stack then push `true` if the first popped value is less than the second, `false` otherwise.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Less(Range range) implements Bytecode {}

    /**
     * Pop two values from the stack then push `true` if the first popped value is greater or equal to the second, `false` otherwise.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record GreaterEqual(Range range) implements Bytecode {}

    /**
     * Pop two values from the stack then push `true` if the first popped value is less or equal to the second, `false` otherwise.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record LessEqual(Range range) implements Bytecode {}

    //Statements

    /**
     * Move forward on the given distance.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Forward(Range range) implements Effectful {}

    /**
     * Move backward on the given distance.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Backward(Range range) implements Effectful {}

    /**
     * A Turn {@code angle}.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Turn(Range range) implements Effectful {}

    /**
     * Teleport the cursor to the given position.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Pos(Range range) implements Effectful {}

    /**
     * Move the current cursor relatively
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Move(Range range) implements Effectful {}

    /**
     * Hide the given cursor.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Hide(Range range) implements Effectful {}

    /**
     * Show the given cursor.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Show(Range range) implements Effectful {}

    /**
     * Set the opacity of the cursor.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Press(Range range) implements Effectful {}

    /**
     * Set the color of the cursor.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Color(Range range) implements Effectful {}

    /**
     * Set the color of the cursor. Takes 3 arguments: red, green, blue.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record ColorRGB(Range range) implements Effectful {}

    /**
     * Set the thickness of the cursor.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Thick(Range range) implements Effectful {}

    /**
     * Make the current cursor look at the given one.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record LookAtCursor(Range range) implements Effectful {}

    /**
     * Make the current cursor look at the given position.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record LookAtPos(Range range) implements Effectful {}

    /**
     * Create a new cursor with the given id.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record CreateCursor(Range range) implements Effectful {}

    /**
     * Select a cursor.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record SelectCursor(Range range) implements Effectful {}

    /**
     * Remove a cursor.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record RemoveCursor(Range range) implements Effectful {}

    /**
     * Create a new cursor mimicking the given one.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record Mimic(Range range) implements Effectful {}

    /**
     * Duplicate the current cursor by making a central symmetry.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record MirrorCentral(Range range) implements Effectful {}

    /**
     * Duplicate the current cursor by making an axial symmetry.
     *
     * @param range the starting and ending {@link Position} of this instruction
     */
    record MirrorAxial(Range range) implements Effectful {}
}
