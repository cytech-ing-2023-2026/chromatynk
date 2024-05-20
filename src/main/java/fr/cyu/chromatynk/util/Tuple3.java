package fr.cyu.chromatynk.util;

/**
 * A tuple of two values.
 *
 * @param a the first value
 * @param b the second value
 * @param c the third value
 * @param <A> the first value's type
 * @param <B> the second value's type
 * @param <C> the third value's type
 */
public record Tuple3<A, B, C>(A a, B b, C c) {}
