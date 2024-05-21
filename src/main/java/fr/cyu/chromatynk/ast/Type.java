package fr.cyu.chromatynk.ast;

import fr.cyu.chromatynk.eval.Value;

import java.util.Optional;

public enum Type {

    BOOLEAN("BOOL", new Value.Bool(false)),
    STRING("STR", new Value.Str("")),
    INT("INT", new Value.Int(0)),
    FLOAT("NUM", new Value.Float(0)),
    PERCENTAGE("PCRT", new Value.Percentage(0)),
    COLOR("CLR", new Value.Color(0, 0, 0, 1));

    final String name;
    final Value defaultValue;

    Type(String name, Value defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public Value getDefaultValue() {
        return defaultValue;
    }

    public boolean isNumeric() {
        return this == INT || this == FLOAT;
    }

    /**
     * Get a type from its name.
     *
     * @param name the textual name of the type
     * @return the type corresponding to the given name
     */
    public static Optional<Type> fromName(String name) {
        for(Type type : Type.values()) {
            if(type.name.equals(name)) return Optional.of(type);
        }

        return Optional.empty();
    }
}