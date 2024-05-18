package fr.cyu.chromatynk;

import fr.cyu.chromatynk.util.Range;

/**
 * An exception occurring while trying to run a Chromat'ynk code.
 */
public class ChromatynkException extends Exception {

    private final Range range;

    /**
     * Create a new Chromat'ynk exception.
     *
     * @param range the range where the error occurred
     * @param message the error message
     */
    public ChromatynkException(Range range, String message) {
        super(message);
        this.range = range;
    }

    /**
     * Get the range where the exception occurred.
     */
    public Range getRange() {
        return range;
    }

    /**
     * Get pretty-printed representation of this exception.
     *
     * @param sourceCode the source code to extract information from
     * @return a pretty-formatted String representation of this exception
     */
    public String getFullMessage(String sourceCode) {
        StringBuilder result = new StringBuilder(getMessage()).append('\n');

        if (range.isSameLine()) {
            String line = range.subLines(sourceCode);
            result
                    .append('\n')
                    .append(line)
                    .append('\n');

            for (int i = 0; i < line.length(); i++) {
                result.append(i >= range.from().column() && i < range.to().column() ? '^' : ' ');
            }

            result.append('\n').append('\n');
        }

        if (range.from().equals(range.to())) {
            result.append("At line ").append(range.from().row()).append(", column ").append(range.from().column());
        } else {
            result.append("From line ").append(range.from().row()).append(", column ").append(range.from().column()).append('\n');
            result.append("To line ").append(range.to().row()).append(", column ").append(range.to().column());
        }

        return result.toString();
    }
}
