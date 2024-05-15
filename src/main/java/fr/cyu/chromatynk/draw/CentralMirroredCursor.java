package fr.cyu.chromatynk.draw;

import javafx.scene.canvas.GraphicsContext;

public class CentralMirroredCursor extends DuplicatedCursor {

    private final double centerX;
    private final double centerY;

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
