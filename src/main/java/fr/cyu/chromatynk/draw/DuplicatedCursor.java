package fr.cyu.chromatynk.draw;

public abstract class DuplicatedCursor implements Cursor {

    private final Cursor duplicated;

    public DuplicatedCursor(Cursor duplicated) {
        this.duplicated = duplicated;
    }

    public Cursor getDuplicated() {
        return duplicated;
    }

    @Override
    public double getX() {
        return duplicated.getX();
    }

    @Override
    public double getY() {
        return duplicated.getY();
    }

    @Override
    public void setX(double x) {
        duplicated.setX(x);
    }

    @Override
    public void setY(double y) {
        duplicated.setY(y);
    }

    @Override
    public double getDirX() {
        return duplicated.getDirX();
    }

    @Override
    public void setDirX(double dirX) {
        duplicated.setDirX(dirX);
    }

    @Override
    public double getDirY() {
        return duplicated.getDirY();
    }

    @Override
    public void setDirY(double dirY) {
        duplicated.setDirY(dirY);
    }

    @Override
    public Color getColor() {
        return duplicated.getColor();
    }

    @Override
    public void setColor(Color color) {
        duplicated.setColor(color);
    }

    @Override
    public double getOpacity() {
        return duplicated.getOpacity();
    }

    @Override
    public void setOpacity(double opacity) {
        duplicated.setOpacity(opacity);
    }

    @Override
    public double getThickness() {
        return duplicated.getThickness();
    }

    @Override
    public void setThickness(double thickness) {
        duplicated.setThickness(thickness);
    }
}
