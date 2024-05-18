package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.ChromatynkException;
import fr.cyu.chromatynk.util.Range;

/**
 * An exception occurring while parsing a wrong input.
 */
public sealed class ParsingException extends ChromatynkException {

    /**
     * Create a new {@link ParsingException}.
     *
     * @param range the range of the parsing failure
     * @param message the error message
     */
    private ParsingException(Range range, String message) {
        super(range, message);
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
        public Fatal(Range position, String message) {
            super(position, message);
        }

        /**
         * Make a {@link ParsingException} fatal.
         *
         * @param exception the exception to wrap
         */
        public Fatal(ParsingException exception) {
            super(exception.getRange(), exception.getMessage());
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
        public NonFatal(Range position, String message) {
            super(position, message);
        }
    }
}