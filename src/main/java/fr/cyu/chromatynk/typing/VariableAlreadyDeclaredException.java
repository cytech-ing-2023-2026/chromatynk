package fr.cyu.chromatynk.typing;

import fr.cyu.chromatynk.util.Range;

public class VariableAlreadyDeclaredException extends TypingException {
    public VariableAlreadyDeclaredException(Range range, String name) {
        super(range, "Already declared variable: " + name);
    }
}
