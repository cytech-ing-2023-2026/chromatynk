package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.util.Position;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link ParsingException} variant where the parser expected a kind of input and got another one.
 */
public class UnexpectedInputException extends ParsingException {

    private final String expected;
    private final String actual;

    /**
     * Create a new unexpected input.
     *
     * @param position the position where the error occurred
     * @param expected the expected type of input
     * @param actual the input actually parsed
     */
    public UnexpectedInputException(Position position, String expected, String actual) {
        super(position, "Unexpected input.\nExpected: " + expected + "\nGot: " + actual);
        this.expected = expected;
        this.actual = actual;
    }

    /**
     * Get expected input.
     *
     * @return the kind of input expected from the throwing {@link Parser}.
     */
    public String getExpected() {
        return expected;
    }

    /**
     * Get actual input.
     *
     * @return the input actually parsed instead of the expected one.
     */
    public String getActual() {
        return actual;
    }

    /**
     * Create an {@link UnexpectedInputException} instance from a {@link Set} of expected elements of type `T`.
     *
     * @param position the position where the error occurred
     * @param expected the expected values
     * @param actual the value actually parsed
     * @return a new {@link UnexpectedInputException} from the given expected values
     * @param <T> the type of the expected values
     */
    public static <T> UnexpectedInputException anyOf(Position position, Set<T> expected, String actual) {
        String expectedString = expected
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        return new UnexpectedInputException(position, "Any of: " + expectedString, actual);
    }

    /**
     * Create an {@link UnexpectedInputException} instance from a {@link Set} of expected elements of type `T`.
     *
     * @param position the position where the error occurred
     * @param expected the expected values
     * @param actual the value actually parsed
     * @return a new {@link UnexpectedInputException} from the given expected values
     * @param <T> the type of the expected values
     */
    public static <T> UnexpectedInputException anyOfValue(Position position, Set<T> expected, T actual) {
        return anyOf(position, expected, actual == null ? "null" : actual.toString());
    }
}
