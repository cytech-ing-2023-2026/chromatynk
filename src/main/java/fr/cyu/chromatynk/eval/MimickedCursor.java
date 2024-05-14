package fr.cyu.chromatynk.eval;

public class MimickedCursor implements Cursor {

    private final Cursor mimicking;
    private final Cursor mimicked;

    public MimickedCursor(Cursor mimicking, Cursor mimicked) {
        this.mimicking = mimicking;
        this.mimicked = mimicked;
    }

    @Override
    public double getX() {
        return mimicked.getX();
    }

    @Override
    public double getY() {
        return mimicked.getY();
    }

    @Override
    public void setX(double x) {
        mimicking.setX(x);
        mimicked.setX(x);
    }

    @Override
    public void setY(double y) {
        mimicking.setY(y);
        mimicked.setY(y);
    }

    @Override
    public double getDirX() {
        return mimicked.getDirX();
    }

    @Override
    public void setDirX(double dirX) {
        mimicking.setDirX(dirX);
        mimicked.setDirX(dirX);
    }

    @Override
    public double getDirY() {
        return mimicked.getDirY();
    }

    @Override
    public void setDirY(double dirY) {
        mimicking.setDirY(dirY);
        mimicked.setDirY(dirY);
    }

    @Override
    public Color getColor() {
        return mimicked.getColor();
    }

    @Override
    public void setColor(Color color) {
        mimicking.setColor(color);
        mimicked.setColor(color);
    }

    @Override
    public double getOpacity() {
        return mimicked.getOpacity();
    }

    @Override
    public void setOpacity(double opacity) {
        mimicking.setOpacity(opacity);
        mimicked.setOpacity(opacity);
    }

    @Override
    public double getThickness() {
        return mimicked.getThickness();
    }

    @Override
    public void setThickness(double thickness) {
        mimicking.setThickness(thickness);
        mimicked.setThickness(thickness);
    }
}
