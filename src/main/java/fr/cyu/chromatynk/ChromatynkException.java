package fr.cyu.chromatynk;

import fr.cyu.chromatynk.util.Range;

/**
 * An exception occurring while trying to run a Chromat'ynk code.
 */
public class ChromatynkException extends Exception {

    private final Range range;
    private final String header;

    /**
     * Create a new Chromat'ynk exception.
     *
     * @param range the range where the error occurred
     * @param message the error message
     */
    public ChromatynkException(Range range, String message, String header) {
        super(message);
        this.range = range;
        this.header = header;
    }

    /**
     * Get the range where the exception occurred.
     */
    public Range getRange() {
        return range;
    }

    /**
     * Get full message's header of this error.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Get pretty-printed representation of this exception.
     *
     * @param sourceCode the source code to extract information from
     * @return a pretty-formatted String representation of this exception
     */
    public String getFullMessage(String sourceCode) {
        StringBuilder result = new StringBuilder(getHeader())
                .append('\n')
                .append(getMessage())
                .append('\n');

        if (range.isSameLine()) {
            String line = range.subLines(sourceCode);
            result
                    .append('\n')
                    .append(line)
                    .append('\n');

            int fromCol = range.from().column();
            int toCol = range.to().column();
            boolean samePos = fromCol == toCol;

            int length = Math.max(line.length(), toCol+1);

            for (int i = 0; i < length; i++) {
                result.append(i >= fromCol && (i < toCol || (i == fromCol && samePos)) ? '^' : ' ');
            }

            result.append('\n').append('\n');
        }

        if (range.from().equals(range.to())) {
            result.append("At line ").append(range.from().row()+1).append(", column ").append(range.from().column());
        } else {
            result.append("From line ").append(range.from().row()+1).append(", column ").append(range.from().column()).append('\n');
            result.append("To line ").append(range.to().row()+1).append(", column ").append(range.to().column());
        }

        return result.toString();
    }
}
