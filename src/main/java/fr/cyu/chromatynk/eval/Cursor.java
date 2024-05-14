package fr.cyu.chromatynk.eval;

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

    default Cursor copyTangible() {
        return new TangibleCursor(getX(), getY(), getDirX(), getDirY(), getColor(), getOpacity(), getThickness());
    }
}
