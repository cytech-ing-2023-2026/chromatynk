package fr.cyu.chromatynk.ast;

import java.util.Optional;

public enum Type {

    BOOLEAN("BOOL"),
    STRING("STR"),
    INT("INT"),
    FLOAT("NUM"),
    COLOR("CLR"),
    PERCENT("PERCENT");

    final String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
