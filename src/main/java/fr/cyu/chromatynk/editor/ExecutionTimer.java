package fr.cyu.chromatynk.editor;

import fr.cyu.chromatynk.Chromatynk;
import fr.cyu.chromatynk.eval.Clock;
import fr.cyu.chromatynk.eval.EvalContext;
import javafx.animation.AnimationTimer;

import java.util.function.Consumer;

/**
 * A timer for executing a script within a given context, using a specified clock for timing.
 * It provides callbacks for success, error, and progress events.
 * 
 * @author Iltotore
 * @see EvalContext
 * @see Clock
 */
public class ExecutionTimer extends AnimationTimer {

    private String source;
    private EvalContext context;
    private Clock clock;
    private Runnable onSuccess;
    private Consumer<Throwable> onError;
    private Consumer<EvalContext> onProgress;

	/**
     * Constructs an {@link ExecutionTimer} with the specified parameters.
     * 
     * @param source the source code to be executed
     * @param context the evaluation context in which the source code is executed
     * @param clock the clock used for timing the execution
     * @param onSuccess the callback to be executed upon successful completion of the script
     * @param onError the callback to be executed if an error occurs during script execution
     * @param onProgress the callback to be executed on each progress step of the script execution
     */
    public ExecutionTimer(String source, EvalContext context, Clock clock, Runnable onSuccess, Consumer<Throwable> onError, Consumer<EvalContext> onProgress) {
        this.source = source;
        this.context = context;
        this.clock = clock;
        this.onSuccess = onSuccess;
        this.onError = onError;
        this.onProgress = onProgress;
    }

	/**
     * Sets the clock used for timing the execution.
     * 
     * @param clock the new clock to be used
     */
    public void setClock(Clock clock) {
        this.clock = clock;
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
