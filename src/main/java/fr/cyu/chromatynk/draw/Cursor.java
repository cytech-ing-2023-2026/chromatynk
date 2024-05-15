package fr.cyu.chromatynk.draw;

import javafx.scene.canvas.GraphicsContext;

public interface Cursor {

    double getX();

    double getY();

    void setX(double x);

    void setY(double y);

    double getDirX();

    void setDirX(double dirX);

    double getDirY();

    void setDirY(double dirY);

    Color getColor();

    void setColor(Color color);

    double getOpacity();

    void setOpacity(double opacity);

    double getThickness();

    void setThickness(double thickness);

    void drawLineAt(GraphicsContext graphics, double x, double y, double dx, double dy);

    default void move(GraphicsContext graphics, double dx, double dy) {
        drawLineAt(graphics, getX(), getY(), getX()+dx, getY()+dy);
    }

    default Cursor copyTangible() {
        return new TangibleCursor(getX(), getY(), getDirX(), getDirY(), getColor(), getOpacity(), getThickness());
    }
}
