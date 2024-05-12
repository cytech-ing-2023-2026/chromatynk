package fr.cyu.chromatynk.parsing;

@FunctionalInterface
public interface ParsingFunction<I, O> {

    O apply(I input) throws ParsingException;
}
