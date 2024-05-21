package fr.cyu.chromatynk.eval;

public class ForeverClock implements Clock {

    @Override
    public boolean tick() {
        return true;
    }
}
