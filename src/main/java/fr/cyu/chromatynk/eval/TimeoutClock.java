package fr.cyu.chromatynk.eval;

public class TimeoutClock implements Clock {

    private final long timeout;
    private long stopTime;


    public TimeoutClock(long timeout) {
        this.timeout = timeout;
        this.stopTime = -1;
    }

    @Override
    public boolean tick(boolean isEffectful) {
        long now = System.currentTimeMillis();
        if(stopTime == -1) stopTime = now + timeout;
        if(now < stopTime) return true;
        else {
            stopTime = -1;
            return false;
        }
    }

    public static TimeoutClock fps(int targetFps) {
        if(targetFps <= 0) throw new IllegalArgumentException("targetFps must be positive");
        return new TimeoutClock(1000/targetFps);
    }

    @Override
    public void reset() {
        stopTime = -1;
    }
}
