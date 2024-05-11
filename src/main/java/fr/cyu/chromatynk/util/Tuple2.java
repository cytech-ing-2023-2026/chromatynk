package fr.cyu.chromatynk.util;

/**
 * A tuple of two values.
 *
 * @param a the first value
 * @param b the second value
 * @param <A> the first value's type
 * @param <B> the second value's type
 */
public record Tuple2<A, B>(A a, B b) {}
