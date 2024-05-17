package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.util.Range;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fr.cyu.chromatynk.parsing.Parser.*;
import static fr.cyu.chromatynk.parsing.Token.*;

/**
 * The lexer of Chromat'ynk. Transforms the source code in a sequence of tokens.
 */
public class Lexer {

    //Literals

    /**
     * Parser for boolean literals (`true` or `false`).
     */
    public static final Parser<Character, LiteralBool> LITERAL_BOOL_PARSER = firstSucceeding(keyword("true"), keyword("false"))
            .map(Boolean::parseBoolean)
            .mapWithRange(LiteralBool::new)
            .mapError(e -> new UnexpectedInputException(e.getPosition(), "true or false", "Invalid boolean"));

    /**
     * Parser for String literals (e.g `"Hello World"`).
     */
    public static final Parser<Character, LiteralString> LITERAL_STRING_PARSER = matching("\"[^\"]*\"")
            .mapWithRange((r, value) -> new LiteralString(r, value.substring(1, value.length() - 1)))
            .mapError(e -> new UnexpectedInputException(e.getPosition(), "Actual string between quotes", ((UnexpectedInputException) e).getActual()));

    /**
     * Parser for integer literals (e.g 1, 2, 54...).
     */
    public static final Parser<Character, LiteralInt> LITERAL_INT_PARSER = matching("[0-9]+")
            .map(Integer::parseInt)
            .mapWithRange(LiteralInt::new)
            .mapError(e -> new UnexpectedInputException(e.getPosition(), "Actual integer", ((UnexpectedInputException) e).getActual()));

    /**
     * Parser for float literals (e.g 1, 1.5, 0.42...).
     */
    public static final Parser<Character, LiteralFloat> LITERAL_FLOAT_PARSER = matching("[0-9]+(\\.[0-9]+)?")
            .map(Double::parseDouble)
            .mapWithRange(LiteralFloat::new)
            .mapError(e -> new UnexpectedInputException(e.getPosition(), "Actual float", ((UnexpectedInputException) e).getActual()));

    /**
     * Parser for color literals (e.g #FFF, #AA0000, #FFFF...). Must have a length of 3, 4, 6 or 8 hexadecimal digits.
     */
    public static final Parser<Character, LiteralColor> LITERAL_COLOR_PARSER = matching("#([0-9a-fA-F]{8}|[0-9a-fA-F]{6}|[0-9a-fA-F]{4}|[0-9a-fA-F]{3})")
            .mapWithRange(LiteralColor::new)
            .mapError(e -> new UnexpectedInputException(e.getPosition(), "Color in format #XXX or #XXXX or #XXXXXX or #XXXXXXXX where X is an hexadecimal digit", ((UnexpectedInputException) e).getActual()));

    //Symbols

    private static final List<String> OPERATORS = List.of(
            "&&",
            "||",
            "==",
            "!=",
            ">=",
            "<=",
            "%",
            "+",
            "-",
            "*",
            "/",
            "!",
            ">",
            "<"
    );

    /**
     * Parser for all non-operator symbols.
     */
    public static final Parser<Character, Operator> OPERATOR_PARSER = firstSucceeding(
            OPERATORS
                    .stream()
                    .map(op -> symbol(op).valueWithRange(r -> new Operator(r, op)))
                    .collect(Collectors.toList())
    ).mapError(e -> new ParsingException.NonFatal(e.getPosition(), "Symbol expected"));

    private static final List<Map.Entry<String, ParsingFunction<Range, Token>>> SYMBOLS = List.of(
            Map.entry("(", ParenthesisOpen::new),
            Map.entry(")", ParenthesisClosed::new),
            Map.entry("{", BraceOpen::new),
            Map.entry("}", BraceClosed::new),
            Map.entry("=", Assign::new),
            Map.entry(",", Comma::new)
    );

    /**
     * Parser for all non-operator symbols.
     */
    public static final Parser<Character, Token> SYMBOL_PARSER = firstSucceeding(
            SYMBOLS
                    .stream()
                    .map(e -> symbol(e.getKey()).valueWithRange(e.getValue()))
                    .collect(Collectors.toList())
    ).mapError(e -> new ParsingException.NonFatal(e.getPosition(), "Symbol expected"));

    //Keywords

    private static final List<Map.Entry<String, ParsingFunction<Range, Token>>> KEYWORDS = List.of(
            Map.entry("FWD", Fwd::new),
            Map.entry("BWD", Bwd::new),
            Map.entry("TURN", Turn::new),
            Map.entry("MOV", Mov::new),
            Map.entry("POS", Pos::new),
            Map.entry("HIDE", Hide::new),
            Map.entry("SHOW", Show::new),
            Map.entry("PRESS", Press::new),
            Map.entry("COLOR", Color::new),
            Map.entry("THICK", Thick::new),
            Map.entry("LOOKAT", LookAt::new),
            Map.entry("CURSOR", Cursor::new),
            Map.entry("SELECT", Select::new),
            Map.entry("REMOVE", Remove::new),
            Map.entry("IF", If::new),
            Map.entry("ELSE", Else::new),
            Map.entry("FOR", For::new),
            Map.entry("FROM", From::new),
            Map.entry("TO", To::new),
            Map.entry("STEP", Step::new),
            Map.entry("WHILE", While::new),
            Map.entry("MIMIC", Mimic::new),
            Map.entry("MIRROR", Mirror::new),
            Map.entry("DEL", Del::new)
    );

    /**
     * Parser for all alphabetic keywords.
     */
    public static final Parser<Character, Token> KEYWORD_PARSER = firstSucceeding(
            KEYWORDS
                    .stream()
                    .map(e -> keyword(e.getKey()).valueWithRange(e.getValue()))
                    .collect(Collectors.toList())
    ).mapError(e -> new ParsingException.NonFatal(e.getPosition(), "Keyword expected"));

    //Misc

    /**
     * Parser for any identifier. An identifier is alphanumeric and must start with an alphabetic character.
     * It can also contain or/and start with underscores like `_abc` or `abc_def`.
     */
    public static final Parser<Character, Identifier> IDENTIFIER_PARSER = matching("([A-Za-z]|_)([A-Za-z0-9]|_)*")
            .mapWithRange(Identifier::new)
            .mapError(e -> new ParsingException.NonFatal(e.getPosition(), "Identifier expected"));


    /**
     * The full parser converting a Character sequence input to a sequence of {@link Token}.
     */
    public static final Parser<Character, List<Token>> TOKENS_PARSER = firstSucceeding(
            LITERAL_BOOL_PARSER,
            LITERAL_STRING_PARSER,
            LITERAL_INT_PARSER,
            LITERAL_FLOAT_PARSER,
            LITERAL_COLOR_PARSER,
            OPERATOR_PARSER,
            SYMBOL_PARSER,
            KEYWORD_PARSER,
            IDENTIFIER_PARSER
    ).repeat().mapWithRange((r, list) -> {
        list.add(new EndOfFile(r.to()));
        return list;
    });


}
