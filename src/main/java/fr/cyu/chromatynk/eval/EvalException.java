package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.util.Range;

public class EvalException extends Exception {

    private final Range range;

    public EvalException(Range range, String message) {
        super(message);
        this.range = range;
    }

    public Range getRange() {
        return range;
    }
}
