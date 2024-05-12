package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.ast.Type;

import java.util.Optional;

public class Variable {

    private final Type type;
    private Value value;

    public Variable(Type type) {
        this.type = type;
    }

    public Variable(Type type, Value value) {
        this(type);
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Optional<Value> getValue() {
        return Optional.ofNullable(value);
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
