package fr.cyu.chromatynk.test.parsing;

import fr.cyu.chromatynk.parsing.Parser;
import fr.cyu.chromatynk.parsing.ParsingException;
import fr.cyu.chromatynk.parsing.ParsingIterator;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    public static <I, O> void assertParse(O expected, Parser<I, O> parser, ParsingIterator<I> iterator) {
        try {
            assertEquals(expected, parser.parse(iterator).value());
        } catch (ParsingException e) {
            throw new AssertionError(e);
        }
    }

    @SafeVarargs
    public static <I, O> void assertParse(O expected, Parser<I, O> parser, I... input) {
        assertParse(expected, parser, ParsingIterator.of(input));
    }

    public static <O> void assertParseString(O expected, Parser<Character, O> parser, String input) {
        assertParse(expected, parser, ParsingIterator.fromString(input));
    }

    public static <I> void assertParseFailure(Class<? extends ParsingException> expected, Parser<I, ?> parser, ParsingIterator<I> iterator) {
        assertThrows(expected, () -> parser.parse(iterator));
    }
}
