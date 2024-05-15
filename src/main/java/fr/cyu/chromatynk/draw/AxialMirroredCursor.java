package fr.cyu.chromatynk.draw;

import javafx.scene.canvas.GraphicsContext;

public class AxialMirroredCursor extends DuplicatedCursor {

    private final double lineX;
    private final double lineY;

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
