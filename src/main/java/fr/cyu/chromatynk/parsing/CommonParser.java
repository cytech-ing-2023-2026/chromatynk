package fr.cyu.chromatynk.parsing;

/**
 * Commonly used parsers.
 */
public class CommonParser {

    /**
     * Accept any token. Alias for `Parser.<Token>any()`.
     */
    public static Parser<Token, Token> anyToken() {
        return Parser.any();
    }

    /**
     * Accept any token of the given type.
     */
    public static <T extends Token> Parser<Token, T> tokenOf(Class<T> allowedType) {
        return anyToken().map(result -> {
            if(allowedType.isInstance(result)) return (T) result;
            else throw new UnexpectedInputException(result.range().from(), "Token " + allowedType.getSimpleName(), result.toString());
        });
    }
}
