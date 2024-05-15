package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.draw.CursorId;
import fr.cyu.chromatynk.util.Range;

public class CursorAlreadyExistsException extends EvalException {
    public CursorAlreadyExistsException(Range range, CursorId id) {
        super(range,"The cursor with id " + id.getStringId() + " already exists");
    }
}