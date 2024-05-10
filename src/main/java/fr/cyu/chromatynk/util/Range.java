package fr.cyu.chromatynk.util;

/**
 * A range between two {@link Position}.
 *
 * @param from the start of the range
 * @param to the end of the range
 */
public record Range(Position from, Position to) {

    /**
     * Create a new {@link Range} starting and ending on the same line.
     *
     * @param from the starting column
     * @param to the ending column
     * @param row the row
     * @return a new {@link Range} starting from (`from`, `row`) to (`to`, `row`)
     */
    public static Range sameLine(int from, int to, int row) {
        return new Range(new Position(from, row), new Position(to, row));
    }

    /**
     * Create a new {@link Range} starting and ending on the same line.
     *
     * @param from the starting column
     * @param to the ending column
     * @return a new {@link Range} starting from (`from`, `row`) to (`to`, `row`)
     */
    public static Range sameLine(int from, int to) {
        return sameLine(from, to, 0);
    }
}
