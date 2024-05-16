package fr.cyu.chromatynk.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A cursor mirrored according to a symmetry center.
 */
public class CentralMirroredCursor extends DuplicatedCursor {

    private final double centerX;
    private final double centerY;

    /**
     * Create a new central-mirrored cursor.
     *
     * @param duplicated the mirrored cursor
     * @param centerX the X coordinate of the center
     * @param centerY the Y coordinate of the center
     */
    public CentralMirroredCursor(Cursor duplicated, double centerX, double centerY) {
        super(duplicated);
        this.centerX = centerX;
        this.centerY = centerY;
    }

    private double getSymmetricX(double x) {
        return centerX + (centerX - x);
    }

    private double getSymmetricY(double y) {
        return centerY + (centerY - y);
    }

    @Override
    public void drawLineAt(GraphicsContext graphics, double x, double y, double dx, double dy) {
        getDuplicated().drawLineAt(graphics, x, y, dx, dy);
        getDuplicated().drawLineAt(graphics, getSymmetricX(x), getSymmetricY(y), getSymmetricX(x), getSymmetricY(y));
    }
}
