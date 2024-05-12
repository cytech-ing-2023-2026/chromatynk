package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.util.Position;

/**
 * An exception occurring while parsing a wrong input.
 */
public sealed class ParsingException extends Exception {

    private final Position position;

    /**
     * Create a new {@link ParsingException}.
     *
     * @param position the position of the parsing failure
     * @param message the error message
     */
    private ParsingException(Position position, String message) {
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

    /**
     * An unrecoverable exception, not caught by error-catching parsers.
     */
    public static final class Fatal extends ParsingException {

        /**
         * Create a new {@link ParsingException}.
         *
         * @param position the position of the parsing failure
         * @param message  the error message
         */
        public Fatal(Position position, String message) {
            super(position, message);
        }

        /**
         * Make a {@link ParsingException} fatal.
         *
         * @param exception the exception to wrap
         */
        public Fatal(ParsingException exception) {
            super(exception.getPosition(), exception.getMessage());
        }
    }

    /**
     * A recoverable error. Can be caught by parsers.
     */
    public static sealed class NonFatal extends ParsingException permits UnexpectedInputException {

        /**
         * Create a new {@link ParsingException}.
         *
         * @param position the position of the parsing failure
         * @param message  the error message
         */
        public NonFatal(Position position, String message) {
            super(position, message);
        }
    }
}