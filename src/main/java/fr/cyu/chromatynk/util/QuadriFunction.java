package fr.cyu.chromatynk.util;

/**
 * A function taking three arguments and returning a result.
 *
 * @param <I1> the first argument's type
 * @param <I2> the second argument's type
 * @param <I3> the third argument's type
 * @param <I4> the fourth argument's type
 * @param <R> the result's type
 */
@FunctionalInterface
public interface QuadriFunction<I1, I2, I3, I4, R> {

    /**
     * Execute this function.
     *
     * @param arg1 the first argument
     * @param arg2 the second argument
     * @param arg3 the third argument
     * @return the result of the execution of this function
     */
    R apply(I1 arg1, I2 arg2, I3 arg3, I4 arg4);
}
