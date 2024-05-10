package fr.cyu.chromatynk.parsing;

@FunctionalInterface
public interface ParsingBiFunction<I1, I2, O> {

    O apply(I1 a, I2 b) throws ParsingException;
}
