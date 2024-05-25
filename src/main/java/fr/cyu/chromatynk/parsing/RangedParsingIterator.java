package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.util.Position;

import java.util.List;
import java.util.function.Predicate;

public class RangedParsingIterator<T extends Ranged> extends ParsingIterator<T> {

    public RangedParsingIterator(List<T> input, int cursor, Position position, Predicate<T> whitespace, Predicate<T> lineSeparator) {
        super(input, cursor, position, whitespace, lineSeparator);
    }

    public RangedParsingIterator(List<T> input) {
        super(input);
    }

    /**
     * Vararg version of {@link RangedParsingIterator#RangedParsingIterator(List)}.
     *
     * @param values the elements to iterate over
     * @param <T> the type of the passed values
     * @return a new {@link ParsingIterator} iterating over the given values
     */
    @SafeVarargs
    public static <T extends Ranged> ParsingIterator<T> ofRanged(T... values) {
        return new RangedParsingIterator<>(List.of(values));
    }

    @Override
    public Position getPosition() {
        return getCursor() == 0 ? new Position(0, 0) : getInput().get(getCursor()-1).range().to();
    }

    @Override
    public ParsingIterator<T> copy() {
        return new RangedParsingIterator<>(getInput(), getCursor(), getPosition(), getWhitespacePredicate(), getLineSeparatorPredicate());
    }
}
