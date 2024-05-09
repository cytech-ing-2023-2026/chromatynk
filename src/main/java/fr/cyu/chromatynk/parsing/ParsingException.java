package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.util.Position;

/**
 * An exception occurring while parsing a wrong input.
 */
public class ParsingException extends Exception {

    private final Position position;

    /**
     * Create a new {@link ParsingException}.
     *
     * @param position the position of the parsing failure
     * @param message the error message
     */
    public ParsingException(Position position, String message) {
        super(message);
        this.position = position;
    }

    /**
     * Get position of the parsing failure.
     *
     * @return the row and column where the parsing failed
     */
    public Position getPosition() {
        return position;
    }
}