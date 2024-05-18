package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.Range;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A copyable iterator which tracks its cursor position in line and column.
 *
 * @param <T> the elements of the iterated input
 */
public class ParsingIterator<T> implements Iterator<T> {

    private final List<T> input;
    private int cursor;
    private Position position;
    private final Predicate<T> whitespace;
    private final Predicate<T> lineSeparator;

    /**
     * Create a new {@link ParsingIterator}.
     *
     * @param input         the input to iterate over
     * @param cursor        the starting index of the iterator
     * @param position      the starting 2D position (column and row) of the iterator
     * @param whitespace    the predicate checking if an input element is a whitespace
     * @param lineSeparator the predicate checking if an input element is a line break
     */
    public ParsingIterator(List<T> input, int cursor, Position position, Predicate<T> whitespace, Predicate<T> lineSeparator) {
        this.input = input;
        this.cursor = cursor;
        this.position = position;
        this.whitespace = whitespace;
        this.lineSeparator = lineSeparator;
    }

    /**
     * Create a new {@link ParsingIterator} from an input, starting at index 0, position (0,0) and with no whitespace/line break check.
     *
     * @param input the input to iterate over
     */
    public ParsingIterator(List<T> input) {
        this(input, 0, new Position(0, 0), x -> false, x -> false);
    }

    /**
     * Get the current cursor position of this iterator.
     *
     * @return the index of the next element to process
     */
    public int getCursor() {
        return cursor;
    }

    /**
     * Set the current cursor position of this iterator.
     *
     * @param cursor the index of the next element to process
     */
    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    /**
     * Get the current position of this iterator.
     *
     * @return the position in column and row of this iterator
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Set the current position of this iterator.
     *
     * @param position the position in column and row of this iterator
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Get this {@link ParsingIterator}'s input.
     *
     * @return the complete input being iterated over, including the processed and the remaining parts
     */
    public List<T> getInput() {
        return input;
    }

    /**
     * Iterate over remaining elements without skipping whitespaces.
     *
     * @param consumer the input elements processor
     * @see ParsingIterator#forEachRemaining(Consumer)
     */
    public void forEachRemainingKeepWhitespaces(Consumer<T> consumer) {
        while (hasNext()) consumer.accept(nextKeepWhitespaces());
    }

    /**
     * Get the next element without "iterating over" it.
     *
     * @return the next element to be processed
     */
    public T peek() {
        if (!hasNext()) throw new NoSuchElementException("End of file");
        return input.get(cursor);
    }

    @Override
    public boolean hasNext() {
        return cursor < input.size();
    }

    /**
     * Iterate over whitespaces and line breaks until the next character is not a whitespace nor a line break.
     */
    public void handleWhitespaces() {
        if (!hasNext()) return;

        if (lineSeparator.test(peek())) {
            position = position.nextRow();
            cursor++;
            handleWhitespaces();
        } else if (whitespace.test(peek())) {
            position = position.nextColumn();
            cursor++;
            handleWhitespaces();
        }
    }

    /**
     * Like {@link ParsingIterator#next()} but without skipping whitespaces/line breaks.
     *
     * @return the next element to be processed, even if it is a whitespace or a line break
     */
    public T nextKeepWhitespaces() {
        T result = peek();
        cursor++;
        position = position.nextColumn();

        return result;
    }

    @Override
    public T next() {
        T result = nextKeepWhitespaces();
        handleWhitespaces();

        return result;
    }

    /**
     * Copy this {@link ParsingIterator}.
     *
     * @return a new instance of {@link ParsingIterator} with the same state as this one
     */
    public ParsingIterator<T> copy() {
        return new ParsingIterator<>(input, cursor, position, whitespace, lineSeparator);
    }

    /**
     * Create a new {@link ParsingIterator} from a {@link String} input.
     *
     * @param input the textual input to iterate over
     * @return a new {@link ParsingIterator} reading the characters of the passed input, using LF/CLRF as line breaks and {@link Character#isWhitespace(char)} for whitespaces
     */
    public static ParsingIterator<Character> fromString(String input) {
        String standardizedInput = input.trim().replaceAll("(\r\n|\r)", "\n");

        List<Character> chars = standardizedInput
                .chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        return new ParsingIterator<>(
                chars,
                0,
                new Position(0, 0),
                Character::isWhitespace, Predicate.isEqual('\n')
        );
    }

    /**
     * Vararg version of {@link ParsingIterator#ParsingIterator(List)}.
     *
     * @param values the elements to iterate over
     * @param <T> the type of the passed values
     * @return a new {@link ParsingIterator} iterating over the given values
     */
    @SafeVarargs
    public static <T> ParsingIterator<T> of(T... values) {
        return new ParsingIterator<>(List.of(values));
    }

    /**
     * End of file.
     */
    public static final String EOF = "End of file";
}
