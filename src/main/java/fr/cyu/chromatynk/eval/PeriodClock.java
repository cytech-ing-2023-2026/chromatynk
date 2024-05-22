package fr.cyu.chromatynk.eval;

public class PeriodClock implements Clock{

    private final long period;
    private long nextTime;

    public PeriodClock(long period) {
        this.period = period;
        this.nextTime = 0;
    }

    @Override
    public boolean tick(boolean isEffectful) {
        long now = System.currentTimeMillis();
        if(!isEffectful || now >= nextTime) {
            nextTime = now + period;
            return true;
        } else {
            return false;
        }
    }
}
