package fr.cyu.chromatynk.eval;

public class PeriodClock implements Clock{

    private final long period;
    private long stopTime;

    public PeriodClock(long period) {
        this.period = period;
        this.stopTime = System.currentTimeMillis() + period;
    }

    public long getStopTime() {
        return stopTime;
    }

    @Override
    public boolean tick() {
        if(System.currentTimeMillis() >= stopTime) {
            stopTime = System.currentTimeMillis() + period;
            return false;
        }

        return true;
    }
}
