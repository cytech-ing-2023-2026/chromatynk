package fr.cyu.chromatynk.typing;

import fr.cyu.chromatynk.util.Range;

public class TypingException extends Exception {

    private final Range range;

    public TypingException(Range range, String message) {
        super(message);
        this.range = range;
    }

    public Range getRange() {
        return range;
    }
}
