package fr.cyu.chromatynk.parsing;

import fr.cyu.chromatynk.util.Position;
import fr.cyu.chromatynk.util.Range;
import fr.cyu.chromatynk.util.Tuple2;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser that can be combined with others.
 *
 * @param <I> the iterator input
 * @param <O> the parser output
 */
public interface Parser<I, O> {

    /**
     * Parse the given input.
     *
     * @param iterator the iterator over the given input
     * @return the parsed output
     * @throws ParsingException if the parser fails to parse the given input
     */
    Result<O> parse(ParsingIterator<? extends I> iterator) throws ParsingException;

    /**
     * Decorate this parser with its range then apply the given function to this parser's output.
     *
     * @param f the function to apply
     * @return a new parser equivalent to this one with f applied to the output
     * @param <T> the output type of `f`
     */
    default <T> Parser<I, T> mapWithRange(ParsingBiFunction<Range, O, T> f) {
        return iterator -> {
            Result<O> result = this.parse(iterator);
            return new Result<>(result.range, f.apply(result.range, result.value));
        };
    }

    /**
     * Apply the given function to this parser's output.
     *
     * @param f the function to apply
     * @return a new parser equivalent to this one with f applied to the output
     * @param <T> the output type of `f`
     */
    default <T> Parser<I, T> map(ParsingFunction<O, T> f) {
        return this.mapWithRange((r, o) -> f.apply(o));
    }

    /**
     * Map non-fatal errors thrown by this parser if any.
     *
     * @param f the function to apply to the eventually thrown error
     * @return a new parser similar to this one but with `f` applied to any error thrown by it
     */
    default Parser<I, O> mapError(Function<ParsingException, ? extends ParsingException> f) {
        return iterator -> {
            try {
                return this.parse(iterator);
            } catch (ParsingException.NonFatal e) {
                throw f.apply(e);
            }
        };
    }

    /**
     * Make this parser unrecoverable in case of error.
     *
     * @return this parser with its errors marked as fatal
     */
    default Parser<I, O> fatal() {
        return mapError(ParsingException.Fatal::new);
    }

    /**
     * Create a value from the parsed range if this parser succeeds.
     *
     * @param f the function to apply on the range
     * @return a new parser equivalent to this one but with the result of `f` as output
     * @param <T> the output type of `f`
     */
    default <T> Parser<I, T> valueWithRange(ParsingFunction<Range, T> f) {
        return mapWithRange((r, v) -> f.apply(r));
    }

    /**
     * Make this parser optional.
     *
     * @return the same parser wrapping the result in a `Optional` and recovering with `Optional.empty()` in case of failure
     */
    default Parser<I, Optional<O>> optional() {
        return iterator -> {
            ParsingIterator<? extends I> copy = iterator.copy();
            try {
                Result<O> result = this.parse(copy);
                iterator.setCursor(copy.getCursor());
                iterator.setPosition(copy.getPosition());
                return new Result<>(result.range, Optional.of(result.value));
            } catch (ParsingException.NonFatal e) {
                return new Result<>(new Range(iterator.getPosition(), iterator.getPosition()), Optional.empty());
            }
        };
    }

    /**
     * Repeat this parser until the first failure.
     *
     * @return the list of all the parsed results before the first failure
     */
    default Parser<I, List<O>> repeat() {
        return iterator -> {
            ParsingIterator<? extends I> lastLeftover = iterator.copy();
            List<O> values = new LinkedList<>();

            Position from = iterator.getPosition();
            Position to = from;

            try {
                //The beginning of the result is the beginning of the first element.
                if(lastLeftover.hasNext()) {
                    ParsingIterator<? extends I> copy = lastLeftover.copy();
                    Result<O> result = this.parse(copy);
                    values.add(result.value);
                    from = result.range.from();
                    to = result.range.to();
                    lastLeftover = copy;
                }

                while (lastLeftover.hasNext()) {
                    ParsingIterator<? extends I> copy = lastLeftover.copy();
                    Result<O> result = this.parse(copy);
                    values.add(result.value);
                    to = result.range.to();
                    lastLeftover = copy;
                }
            } catch (ParsingException.NonFatal ignored) {}

            iterator.setCursor(lastLeftover.getCursor());
            iterator.setPosition(lastLeftover.getPosition());

            return new Result<>(new Range(from, to), values);
        };
    }

    /**
     * Repeat this parser until the given one succeeds.
     *
     * @param parser the parser signaling the end of the repetition
     * @return a parser parsing zero or multiple outputs of this parser until it fails or `parser` succeeds
     */
    default Parser<I, List<O>> repeatUntil(Parser<I, ?> parser) {
        Parser<I, ?> stopIfEnd = parser
                .optional()
                .mapWithRange((r, opt) -> {
                    if(opt.isPresent()) throw new ParsingException.NonFatal(r.from(), "End");
                    else return opt;
                });

        return this.prefixed(stopIfEnd).repeat();
    }

    /**
     * Repeat and reduce this parser according to the reduction produced when parsing the separator.
     * Useful for parsing binary operators in a recursion-friendly way.
     *
     * @param reductionParser the separator parser used to determine how to reduce two results in a single one
     * @return a parser using this one to parser elements reduced according to the result of the parsing of their separators
     */
    default Parser<I, O> repeatReduce(Parser<I, BiFunction<O, O, O>> reductionParser) {
        return this.zip(reductionParser.zip(this).repeat()).map(tpl -> {
            O result = tpl.a();
            List<Tuple2<BiFunction<O, O, O>, O>> reductions = tpl.b();
            for(Tuple2<BiFunction<O, O, O>, O> reduction : reductions) {
                result = reduction.a().apply(result, reduction.b());
            }

            return result;
        });
    }

    /**
     * Right-zip a parser to this one.
     *
     * @param next the parser to zip to this one
     * @return a new parser using this one then `next`, zipping the results in a tuple.
     * @param <O2> the output type of `next`
     */
    default <O2> Parser<I, Tuple2<O, O2>> zip(Parser<I, O2> next) {
        return iterator -> {
            Result<O> first = this.parse(iterator);
            Result<O2> second = next.parse(iterator);

            return new Result<>(first.range.merge(second.range), new Tuple2<>(first.value, second.value));
        };
    }

    /**
     * Require a suffix for this parser.
     *
     * @param parser the suffix parser
     * @return this parser, requiring the given parser to succeed after but still keeping the original output
     */
    default Parser<I, O> suffixed(Parser<I, ?> parser) {
        return iterator -> {
            Result<O> result = this.parse(iterator);
            Result<?> suffix = parser.parse(iterator);
            return new Result<>(result.range.merge(suffix.range), result.value);
        };
    }

    /**
     * Require a prefix for this parser.
     *
     * @param parser the prefix parser
     * @return this parser, requiring the given parser to succeed before but still keeping the original output
     */
    default Parser<I, O> prefixed(Parser<I, ?> parser) {
        return iterator -> {
            Result<?> prefix = parser.parse(iterator);
            Result<O> result = this.parse(iterator);
            return new Result<>(result.range.merge(prefix.range), result.value);
        };
    }

    /**
     * Debugging decorator. Should eventually not be used in production but useful for debugging combinators.
     *
     * @return a new parser similar to this one printing input state and result/error in STDIN.
     */
    default Parser<I, O> debug(String name) {
        return iterator -> {
            String fullName = name + "@" + this.hashCode();
            System.out.println("--- START " + fullName + " ---");
            System.out.println("Position: " + iterator.getPosition());
            System.out.println("Cursor: " + iterator.getCursor());
            System.out.println("Input: " + (iterator.hasNext() ? iterator.peek() : "null"));
            try {
                Result<O> result = this.parse(iterator);
                System.out.println("--- RESULT " + fullName + " ---");
                System.out.println("Result: " + result);
                System.out.println("Next cursor: " + iterator.getCursor());
                return result;
            } catch (ParsingException.NonFatal e) {
                System.out.println("--- ERROR " + name + " ---");
                System.out.println("Exception at " + e.getPosition() + ": " + e.getMessage());
                System.out.println("Next cursor: " + iterator.getCursor());
                throw e;
            }
        };
    }

    /**
     * A parser returning the given result.
     *
     * @param value the result
     * @return a parser consuming no input and returning the given value
     * @param <I> the input type of this parser (despite consuming nothing)
     * @param <O> the result type of this parser
     */
    static <I, O> Parser<I, O> pure(O value) {
        return iterator -> new Result<>(new Range(iterator.getPosition(), iterator.getPosition()), value);
    }

    /**
     * Parse any token.
     *
     * @return a parser returning the next token
     * @param <T> the token type
     */
    static <T> Parser<T, T> any() {
        return iterator -> {
            if(!iterator.hasNext()) throw new ParsingException.NonFatal(iterator.getPosition(), ParsingIterator.EOF);
            Position start = iterator.getPosition();
            T value = iterator.next();
            return new Result<>(new Range(start, iterator.getPosition()), value);
        };
    }

    /**
     * Parse any of the given values.
     *
     * @param expected the allowed tokens
     * @return the parsed token if it is contained in `expected`
     * @param <T> the token type
     */
    static <T> Parser<T, T> anyOfSet(Set<T> expected) {
        return iterator -> {
            if(!iterator.hasNext()) throw UnexpectedInputException.anyOf(iterator.getPosition(), expected, ParsingIterator.EOF);
            else {
                Position start = iterator.getPosition();
                T value = iterator.next();
                if(expected.contains(value)) return new Result<>(new Range(start, iterator.getPosition()), value);
                else throw UnexpectedInputException.anyOfValue(iterator.getPosition(), expected, value);
            }
        };
    }

    /**
     * Parse any of the given values.
     *
     * @param expected the allowed tokens
     * @return the parsed token if it is contained in `expected`
     * @param <T> the token type
     */
    @SafeVarargs
    static <T> Parser<T, T> anyOf(T... expected) {
        return anyOfSet(Set.of(expected));
    }

    /**
     * A lazy proxy for a parser. Useful for recursive parsers like the one used for syntactic processing.
     *
     * @param lazyParser the parser supplier that will provide an instance when needed.
     * @return a proxy for the given parser supplier
     * @param <I> the parser input
     * @param <O> the parser output
     */
    static <I, O> Parser<I, O> lazy(Supplier<Parser<I, O>> lazyParser) {
        return iterator -> lazyParser.get().parse(iterator);
    }

    /**
     * A parser returning the result of the first succeeding parser.
     *
     * @param parsers the parsers to try.
     * @return a parser trying each parser in the given order until one succeeds.
     * @param <I> the parser input
     * @param <O> the common output type of each parser
     */
    static <I, O> Parser<I, O> firstSucceeding(Iterable<Parser<I, ? extends O>> parsers) {
        return iterator -> {
            for(Parser<I, ? extends O> parser : parsers) {
                try {
                    ParsingIterator<? extends I> parserIterator = iterator.copy();
                    Result<? extends O> result = parser.parse(parserIterator);
                    iterator.setCursor(parserIterator.getCursor());
                    iterator.setPosition(parserIterator.getPosition());

                    //Cast safe since Parser is supposed to be covariant in `O`.
                    return (Result<O>) result;
                } catch (ParsingException.NonFatal ignored) {}
            }
            
            throw new ParsingException.NonFatal(iterator.getPosition(), "No parser succeeded");
        };
    }

    /**
     * A parser returning the result of the first succeeding parser.
     *
     * @param parsers the parsers to try.
     * @return a parser trying each parser in the given order until one succeeds.
     * @param <I> the parser input
     * @param <O> the common output type of each parser
     */
    @SafeVarargs
    static <I, O> Parser<I, O> firstSucceeding(Parser<I, ? extends O>... parsers) {
        return firstSucceeding(Arrays.asList(parsers));
    }

    /**
     * A parser matching a String with the given pattern.
     *
     * @param regex the pattern to match against
     * @return a parser accepting the first group matching the given regex
     */
    static Parser<Character, String> matching(String regex) {
        Pattern pattern = Pattern.compile(regex);
        return iterator -> {
            StringBuilder remainingInput = new StringBuilder();
            iterator.copy().forEachRemainingKeepWhitespaces(remainingInput::append);

            Matcher matcher = pattern.matcher(remainingInput.toString());
            if(matcher.find() && matcher.start() == 0) {
                int matcherEnd = iterator.getCursor()+matcher.end();
                Position from = iterator.getPosition();
                while (iterator.getCursor() < matcherEnd) iterator.nextKeepWhitespaces();
                Position to = iterator.getPosition();

                iterator.handleWhitespaces();

                return new Result<>(new Range(from, to), remainingInput.substring(matcher.start(), matcher.end()));
            } else throw new UnexpectedInputException(
                    iterator.getPosition(),
                    "String matching regex: " + regex.replace("\\", "\\\\").translateEscapes(),
                    remainingInput.toString()
            );
        };
    }

    /**
     * An alphabetic keyword parser.
     * For keyword "keyword":
     *
     * <pre>
     *     keyword     //matches "keyword"
     *     keyword abc //matches "keyword"
     *     keyword()   //matches "keyword"
     *     keywordabc  //does not match
     * </pre>
     *
     *
     *
     *
     * @param word the keyword to parse
     * @return a parser outputting the given keyword if the input matches it.
     * @see {@link #symbol(String)}
     */
    static Parser<Character, String> keyword(String word) {
        return iterator -> {
            if(!iterator.hasNext()) throw new UnexpectedInputException(iterator.getPosition(), word, ParsingIterator.EOF);
            int matching = 0;
            Position from = iterator.getPosition();

            while(iterator.hasNext() && Character.isAlphabetic(iterator.peek())) {
                    char next = iterator.nextKeepWhitespaces();
                    if(matching < word.length() && word.charAt(matching) == next) matching++;
                    else throw new UnexpectedInputException(iterator.getPosition(), word, word.substring(0, matching) + next);
            }

            if(matching != word.length()) throw new UnexpectedInputException(iterator.getPosition(), word, word.substring(0, matching));
            Position to = iterator.getPosition();

            iterator.handleWhitespaces();

            return new Result<>(new Range(from, to), word);
        };
    }

    /**
     * A symbol parser. For symbol "+":
     *
     * <pre>
     *     +     //matches "+"
     *     + abc //matches "+"
     *     +abc  //matches "+"
     *     +()   //matches "+"
     *     +=    //matches "+"
     * </pre>
     *
     * @param symbol the keyword to parse
     * @return a parser outputting the given symbol if the input matches it.
     * @see {@link #keyword(String)}
     */
    static Parser<Character, String> symbol(String symbol) {
        return iterator -> {
            if(!iterator.hasNext()) throw new UnexpectedInputException(iterator.getPosition(), symbol, ParsingIterator.EOF);
            int matching = 0;

            Position from = iterator.getPosition();

            while(iterator.hasNext() && matching < symbol.length()) {
                char next = iterator.nextKeepWhitespaces();
                if(symbol.charAt(matching) == next) matching++;
                else throw new UnexpectedInputException(iterator.getPosition(), symbol, symbol.substring(0, matching) + next);
            }

            if(matching != symbol.length()) throw new UnexpectedInputException(iterator.getPosition(), symbol, symbol.substring(0, matching));

            Position to = iterator.getPosition();

            iterator.handleWhitespaces();

            return new Result<>(new Range(from, to), symbol);
        };
    }

    /**
     * A parsing result.
     *
     * @param range the starting and ending position of the parsed value
     * @param value the parsed value
     * @param <T> the type of parsed value
     */
    record Result<T>(Range range, T value) {}
}
