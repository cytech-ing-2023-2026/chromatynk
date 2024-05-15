package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.draw.CursorId;
import fr.cyu.chromatynk.util.Range;

public class MissingCursorException extends EvalException {

    public MissingCursorException(Range range, CursorId id) {
        super(range, "No such cursor: " + id.getStringId());
    }
}
