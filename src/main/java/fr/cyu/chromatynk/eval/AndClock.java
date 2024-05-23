package fr.cyu.chromatynk.eval;

public class AndClock implements Clock {

    private final Clock clockA;
    private final Clock clockB;

    public AndClock(Clock clockA, Clock clockB) {
        this.clockA = clockA;
        this.clockB = clockB;
    }

    @Override
    public boolean tick(boolean isEffectful) {
        return clockA.tick(isEffectful) && clockB.tick(isEffectful);
    }
}
