package fr.cyu.chromatynk.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A cursor mirrored according to a symmetry axis.
 */
public class AxialMirroredCursor extends DuplicatedCursor {

    private final double lineX;
    private final double lineY;

    /**
     * Create a new axial-mirrored cursor.
     *
     * @param duplicated the mirrored cursor
     * @param lineX the X direction of the axis
     * @param lineY the Y direction of the axis
     */
    public AxialMirroredCursor(Cursor duplicated, double lineX, double lineY) {
        super(duplicated);
        this.lineX = lineX;
        this.lineY = lineY;
    }

    private double getSymmetricX(double x, double y) {
        double factorToY = y / lineY;
        double xAtY = lineX*factorToY;
        return xAtY + (xAtY - x);
    }

    private double getSymmetricY(double x, double y) {
        double factorToX = x / lineX;
        double yAtX = lineY*factorToX;
        return yAtX + (yAtX - y);
    }

    @Override
    public void drawLineAt(GraphicsContext graphics, double x, double y, double dx, double dy) {
        getDuplicated().drawLineAt(graphics, x, y, dx, dy);
        getDuplicated().drawLineAt(graphics, getSymmetricX(x, y), getSymmetricY(x, y), getSymmetricX(dx, dy), getSymmetricY(dx, dy));
    }
}
