package fr.cyu.chromatynk.test.parsing;

import fr.cyu.chromatynk.parsing.ParsingIterator;
import fr.cyu.chromatynk.util.Position;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class ConsumptionTestCase {

    @Test
    public void fromString() {
        assertEquals(List.of('a', 'b', 'c'), ParsingIterator.fromString("abc").getInput(), "non-empty String");
        assertEquals(List.of(), ParsingIterator.fromString("").getInput(), "Empty String");
    }

    @Test
    public void next() {
        ParsingIterator<Character> iterator = ParsingIterator.fromString("a");
        assertTrue(iterator.hasNext(), "hasNext non-empty input");
        assertEquals('a', iterator.next(), "next non-empty input");
        assertFalse(iterator.hasNext(), "hasNext non-empty input");
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void nextLine() {
        ParsingIterator<Character> iterator = ParsingIterator.fromString("ab\ncd");
        assertEquals(new Position(0, 0), iterator.getPosition(), "0,0");
        iterator.next();
        assertEquals(new Position(1, 0), iterator.getPosition(), "0,1");
        iterator.next();
        assertEquals(new Position(0, 1), iterator.getPosition(), "1,0");
        iterator.next();
        assertEquals(new Position(1, 1), iterator.getPosition(), "1,1");
        assertEquals('d', iterator.next());
    }
}
