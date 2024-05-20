package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.util.Range;

public class InvalidAddressException extends EvalException {

    public InvalidAddressException(Range range, int address) {
        super(range, "Invalid bytecode address: " + address);
    }
}
