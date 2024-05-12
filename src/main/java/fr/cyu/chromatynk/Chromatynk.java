package fr.cyu.chromatynk;

import fr.cyu.chromatynk.ast.Program;
import fr.cyu.chromatynk.parsing.Lexer;
import fr.cyu.chromatynk.parsing.ParsingException;
import fr.cyu.chromatynk.parsing.ParsingIterator;
import fr.cyu.chromatynk.parsing.StatementParser;

public class Chromatynk {

    /**
     * Parse a program from the given source.
     *
     * @param source the source code to parse
     * @return the parsed program
     * @throws ParsingException
     */
    public static Program parseProgram(String source) throws ParsingException {
        return StatementParser
                .program()
                .parse(new ParsingIterator<>(Lexer.TOKENS_PARSER.parse(ParsingIterator.fromString(source)).value()))
                .value();
    }
}
