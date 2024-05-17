package fr.cyu.chromatynk.bytecode;

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

    //Control

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
    record Store(Range range, String name) implements Bytecode {}

    /**
     * Delete a variable.
     *
     * @param range the starting and ending {@link Position} of this instruction
     * @param name the name of the variable to delete
     */
    record Delete(Range range, String name) implements Bytecode {}

    /**
     * Jump to the given address.
     *
     * @param range the starting and ending {@link Position} of this instruction
     * @param address the address to go to
     */
    record GoTo(Range range, int address) implements Bytecode {}

    /**
     * Pop a value from the stack then jump to an address or another depending on the popped value.
     *
     * @param range the starting and ending {@link Position} of this instruction
     * @param addressTrue the address to jump to if the popped value is `true`
     * @param addressFalse the address to jump to if the popped value is `false`
     */
    record IfElse(Range range, int addressTrue, int addressFalse) implements Bytecode {}

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

    //TODO javadoc

    record Forward(Range range) implements Bytecode {}
    record Backward(Range range) implements Bytecode {}
    record Turn(Range range) implements Bytecode {}
    record Pos(Range range) implements Bytecode {}
    record Move(Range range) implements Bytecode {}
    record Hide(Range range) implements Bytecode {}
    record Show(Range range) implements Bytecode {}

}
