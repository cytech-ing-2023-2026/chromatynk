package fr.cyu.chromatynk.test.parsing;

import fr.cyu.chromatynk.parsing.Parser;
import fr.cyu.chromatynk.parsing.ParsingException;
import fr.cyu.chromatynk.parsing.ParsingIterator;
import fr.cyu.chromatynk.parsing.UnexpectedInputException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.BiFunction;

import static fr.cyu.chromatynk.parsing.Parser.*;
import static fr.cyu.chromatynk.test.parsing.ParserTest.*;

public class CombinatorTestCase {

    @Test
    public void alwaysValidParser() {
        assertParse(1, pure(1), ParsingIterator.of());
        assertParse(1, pure(1), ParsingIterator.fromString("abcd"));
    }

    @Test
    public void anyOfParser() {
        Parser<Integer, Integer> parser = anyOf(1, 2, 3);
        assertParse(1, parser, 1);
        assertParse(2, parser, 2);
        assertParse(3, parser, 3);
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.of(4));
    }

    @Test
    public void firstSucceedingParser() {
        Parser<Character, String> parser = firstSucceeding(List.of(keyword("VARIABLE"), keyword("VAR"), keyword("FWD")));
        assertParseString("VARIABLE", parser, "VARIABLE");
        assertParseString("VAR", parser, "VAR");
        assertParseString("FWD", parser, "FWD");
        assertParseFailure(ParsingException.class, parser, ParsingIterator.fromString(""));
        assertParseFailure(ParsingException.class, parser, ParsingIterator.fromString("FUNCTION"));
    }

    @Test
    public void matchingParser() {
        Parser<Character, String> parser = matching("[a-z]+");
        assertParseString("a", parser, "a");
        assertParseString("abc", parser, "abc");
        assertParseString("abc", parser, "abc5def");
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString(""));
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString("5"));
    }

    @Test
    public void keywordParser() {
        Parser<Character, String> parser = keyword("FWD");
        assertParseString("FWD", parser, "FWD");
        assertParseString("FWD", parser, " FWD ");
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString(""));
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString("F WD"));
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString("PW"));
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString("BWD"));
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString("PWDBWD"));
    }

    @Test
    public void symbolParser() {
        Parser<Character, String> plusParser = symbol("+");
        assertParseString("+", plusParser, "+");
        assertParseString("+", plusParser, " + ");
        assertParseString("+", plusParser, "+=");
        assertParseFailure(UnexpectedInputException.class, plusParser, ParsingIterator.fromString(""));

        Parser<Character, String> plusEqualParser = symbol("+=");
        assertParseString("+=", plusEqualParser, "+=");
        assertParseString("+=", plusEqualParser, " += ");
        assertParseFailure(UnexpectedInputException.class, plusEqualParser, ParsingIterator.fromString(""));
        assertParseFailure(UnexpectedInputException.class, plusEqualParser, ParsingIterator.fromString("+"));
    }

    @Test
    public void map() {
        Parser<Integer, Integer> parser = Parser.<Integer>any().map(x -> x*2);
        assertParse(2, parser, 1);
        assertParse(4, parser, 2);
    }

    @Test
    public void repeat() {
        Parser<Integer, List<Integer>> parser = anyOf(1).repeat();
        assertParse(List.of(1), parser, 1);
        assertParse(List.of(1, 1), parser, 1, 1);
        assertParse(List.of(1, 1), parser, 1, 1, 2);
        assertParse(List.of(), parser, 2, 1, 2);
        assertParse(List.of(), parser);
    }

    @Test
    public void repeatWithSep() {
        //10 is addition
        //11 is subtraction
        Parser<Integer, BiFunction<Integer, Integer, Integer>> operatorParser = Parser.anyOf(10, 11)
                .mapWithRange((r, v) -> switch (v) {
                    case 10 -> (a, b) -> a + b;
                    case 11 -> (a, b) -> a-b;
                    default -> throw new UnexpectedInputException(r, "Valid operator", String.valueOf(v));
                });

        Parser<Integer, Integer> parser = Parser.<Integer>any().repeatReduce(operatorParser);

        assertParse(5, parser, 3, 10, 2);
        assertParse(1, parser, 3, 11, 2);
        assertParse(1, parser, 3, 10, 2, 11, 4);
    }

    @Test
    public void prefixed() {
        Parser<Character, String> parser = keyword("abc").prefixed(keyword("BEGIN"));
        assertParseString("abc", parser, "BEGIN abc");
        assertParseString("abc", parser, "BEGIN\nabc");
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString("abc"));
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString(""));
    }

    @Test
    public void suffixed() {
        Parser<Character, String> parser = keyword("abc").suffixed(keyword("END"));
        assertParseString("abc", parser, "abc END");
        assertParseString("abc", parser, "abc\nEND");
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString("abc"));
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString("abcEND"));
        assertParseFailure(UnexpectedInputException.class, parser, ParsingIterator.fromString(""));
    }
}
