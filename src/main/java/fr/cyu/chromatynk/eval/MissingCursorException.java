package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.draw.CursorId;
import fr.cyu.chromatynk.util.Range;

/**
 * The referenced cursor does not exist.
 */
public class MissingCursorException extends EvalException {

    /**
     * Create a new "missing cursor" exception.
     *
     * @param range the range where the error occurred
     * @param id the id of the missing cursor
     */
    public MissingCursorException(Range range, CursorId id) {
        super(range, "No such cursor: " + id.getStringId());
    }
}
