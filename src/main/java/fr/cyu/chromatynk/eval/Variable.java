package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;

/**
 * A typed variable.
 */
public class Variable {

    private final Type type;
    private Value value;

    /**
     * Create a new uninitialized variable.
     *
     * @param type the variable's type
     */
    public Variable(Type type) {
        this.type = type;
    }

    /**
     * Create a new initialized variable.
     *
     * @param type the variable's type
     * @param value the variable's initialization value
     */
    public Variable(Type type, Value value) {
        this(type);
        this.value = value;
    }

    /**
     * Get the type of this variable.
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the value of this variable if it exists.
     */
    public Value getValue() {
        return value;
    }

    /**
     * Reassign this variable
     *
     * @param value the new value of this variable
     */
    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
