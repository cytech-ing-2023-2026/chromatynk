package fr.cyu.chromatynk.parsing;

/**
 * Commonly used parsers.
 */
public class CommonParser {

    /**
     * Accept any token. Alias for {@code Parser.<Token>any()}.
     */
    public static Parser<Token, Token> anyToken() {
        return Parser.any();
    }

    /**
     * Accept any token of the given type.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Token> Parser<Token, T> tokenOf(Class<T> allowedType, String expectedLabel) {
        return anyToken().map(result -> {
            if(allowedType.isInstance(result)) return (T) result;
            else throw new UnexpectedInputException(result.range(), expectedLabel, result.toPrettyString());
        });
    }

    /**
     * Alias for {@code tokenOf(allowedType, allowedType.getSimpleName())}
     */
    public static <T extends Token> Parser<Token, T> tokenOf(Class<T> allowedType) {
        return tokenOf(allowedType, allowedType.getSimpleName());
    }
}
