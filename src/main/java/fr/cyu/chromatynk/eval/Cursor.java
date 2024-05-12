package fr.cyu.chromatynk.eval;

import fr.cyu.chromatynk.util.Position;

/**
 * Represents a cursor in a 2D plane.
 */
public class Cursor {

    private Position position;

    public Cursor(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
