package fr.cyu.chromatynk.util;

/**
 * A 2D position.
 *
 * @param column the column/character coordinate
 * @param row the row/line coordinate
 */
public record Position(int column, int row) {

    /**
     * Go the next column.
     *
     * @return a new {@link Position} with its column value incremented
     */
    public Position nextColumn() {
        return new Position(column + 1, row);
    }

    /**
     * Go the next row.
     *
     * @return a new {@link Position} with its row value incremented and its column set to 0
     */
    public Position nextRow() {
        return new Position(0, row + 1);
    }

    public boolean isBefore(Position other) {
        return row < other.row || (row == other.row && column < other.column);
    }

    /**
     * Get the smallest position.
     *
     * @param a the first position to compare
     * @param b the second position to compare
     * @return `a` if `a` is smaller, `b` otherwise
     */
    public static Position min(Position a, Position b) {
        return a.isBefore(b) ? a : b;
    }

    /**
     * Get the greatest position.
     *
     * @param a the first position to compare
     * @param b the second position to compare
     * @return `a` if `a` is greater, `b` otherwise
     */
    public static Position max(Position a, Position b) {
        return a.isBefore(b) ? b : a;
    }
}
