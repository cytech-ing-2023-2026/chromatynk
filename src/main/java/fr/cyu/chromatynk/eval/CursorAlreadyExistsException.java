package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.draw.CursorId;
import fr.cyu.chromatynk.util.Range;

/**
 * Tried to declare an already-existing cursor.
 */
public class CursorAlreadyExistsException extends EvalException {

    /**
     * Create a new "double declaration of a cursor" exception.
     *
     * @param range the range where the error occurred
     * @param id    the id of the already-existing cursor
     */
    public CursorAlreadyExistsException(Range range, CursorId id) {
        super(range, "The cursor with id " + id.getStringId() + " already exists");
    }
}