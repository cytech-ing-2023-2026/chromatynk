package fr.cyu.chromatynk.typing;

import fr.cyu.chromatynk.util.Range;

public class MissingVariableException extends TypingException {
    public MissingVariableException(Range range, String name) {
        super(range, "Missing variable: " + name);
    }
}
