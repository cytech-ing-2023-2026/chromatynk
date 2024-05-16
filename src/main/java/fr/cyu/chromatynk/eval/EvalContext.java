package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.draw.Cursor;
import fr.cyu.chromatynk.draw.CursorId;

/**
 * An evaluation context.
 */
public class EvalContext {

    private final Scope scope;
    private CursorId currentCursorId;

    /**
     * Create a new evaluation context.
     *
     * @param scope this context's scope
     * @param currentCursorId the currently selected cursor's id
     */
    public EvalContext(Scope scope, CursorId currentCursorId) {
        this.scope = scope;
        this.currentCursorId = currentCursorId;
    }

    /**
     * Get the current cursor's id.
     *
     * @return the ID of the cursor currently selected
     */
    public CursorId getCurrentCursorId() {
        return currentCursorId;
    }

    public void setCurrentCursorId(CursorId currentCursorId) {
        this.currentCursorId = currentCursorId;
    }

    /**
     * Get the current cursor.
     *
     * @return the cursor in scope associated with the currently selected ID.
     */
    public Cursor getCurrentCursor() {
        return getScope().getCursor(currentCursorId).get();
    }

    /**
     * Get the scope of this context.
     *
     * @return the variables and cursors in scope
     */
    public Scope getScope() {
        return scope;
    }
}
