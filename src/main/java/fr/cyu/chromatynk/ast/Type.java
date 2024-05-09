package fr.cyu.chromatynk.ast;

public enum Type {

    BOOLEAN("BOOL"), STRING("STR"), INT("INT"), FLOAT("NUM"), COLOR("CLR");

    final String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
