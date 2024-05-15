package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.draw.Cursor;
import fr.cyu.chromatynk.draw.CursorId;

public class EvalContext {

    private final Scope scope;
    private CursorId currentCursorId;

    public EvalContext(Scope scope, CursorId currentCursorId) {
        this.scope = scope;
        this.currentCursorId = currentCursorId;
    }

    public CursorId getCurrentCursorId() {
        return currentCursorId;
    }

    public void setCurrentCursorId(CursorId currentCursorId) {
        this.currentCursorId = currentCursorId;
    }

    public Cursor getCurrentCursor() {
        return getScope().getCursor(currentCursorId).get();
    }

    public Scope getScope() {
        return scope;
    }
}
