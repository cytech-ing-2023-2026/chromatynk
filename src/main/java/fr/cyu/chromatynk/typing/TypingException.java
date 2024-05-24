package fr.cyu.chromatynk.typing;

import fr.cyu.chromatynk.ChromatynkException;
import fr.cyu.chromatynk.util.Range;

public class TypingException extends ChromatynkException {

    public TypingException(Range range, String message) {
        super(range, "Typing error.\n" + message);
    }
}
