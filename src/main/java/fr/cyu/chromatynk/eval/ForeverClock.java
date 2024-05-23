package fr.cyu.chromatynk.eval;

public class ForeverClock implements Clock {

    @Override
    public boolean tick(boolean isEffectful) {
        return true;
    }
}
