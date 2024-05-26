package fr.cyu.chromatynk.eval;

public interface Clock {

    /**
     * Tick this clock.
     *
     * @param isEffectful whether the next instruction has a side effect or not
     * @return `true` if the interpretation should continue
     */
    boolean tick(boolean isEffectful);

    /**
     * Reset the state of this clock.
     */
    void reset();
}
