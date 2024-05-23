package fr.cyu.chromatynk.eval;

public class StepByStepClock implements Clock{

    private boolean resumed;

    public StepByStepClock(boolean resumed) {
        this.resumed = resumed;
    }

    public void resume() {
        resumed = true;
    }

    public void pause() {
        resumed = false;
    }

    @Override
    public boolean tick(boolean isEffectful) {
        if(!isEffectful) return true;
        if(resumed) {
            pause();
            return true;
        }

        return false;
    }
}
