package fr.cyu.chromatynk.editor;

import fr.cyu.chromatynk.Chromatynk;
import fr.cyu.chromatynk.eval.Clock;
import fr.cyu.chromatynk.eval.EvalContext;
import javafx.animation.AnimationTimer;

import java.util.function.Consumer;

public class ExecutionTimer extends AnimationTimer {

    private String source;
    private EvalContext context;
    private Clock clock;
    private Runnable onSuccess;
    private Consumer<Throwable> onError;
    private Consumer<EvalContext> onProgress;

    public ExecutionTimer(String source, EvalContext context, Clock clock, Runnable onSuccess, Consumer<Throwable> onError, Consumer<EvalContext> onProgress) {
        this.source = source;
        this.context = context;
        this.clock = clock;
        this.onSuccess = onSuccess;
        this.onError = onError;
        this.onProgress = onProgress;
    }

    @Override
    public void handle(long l) {
        try {
            Chromatynk.execute(context, clock);
            onProgress.accept(context);
            if(!context.hasNext()) {
                onSuccess.run();
                stop();
            }
        } catch (Throwable t) {
            onError.accept(t);
            stop();
        }
    }
}
