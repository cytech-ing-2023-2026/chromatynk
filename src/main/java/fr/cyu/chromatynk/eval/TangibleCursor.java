package fr.cyu.chromatynk.eval;

public class TangibleCursor implements Cursor {

    private double x;
    private double y;
    private double dirX;
    private double dirY;
    private Color color;
    private double opacity;
    private double thickness;

    public TangibleCursor(double x, double y, double dirX, double dirY, Color color, double opacity, double thickness) {
        this.x = x;
        this.y = y;
        this.dirX = dirX;
        this.dirY = dirY;
        this.color = color;
        this.opacity = opacity;
        this.thickness = thickness;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getDirX() {
        return dirX;
    }

    @Override
    public void setDirX(double dirX) {
        this.dirX = dirX;
    }

    @Override
    public double getDirY() {
        return dirY;
    }

    @Override
    public void setDirY(double dirY) {
        this.dirY = dirY;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public double getOpacity() {
        return opacity;
    }

    @Override
    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    @Override
    public double getThickness() {
        return thickness;
    }

    @Override
    public void setThickness(double thickness) {
        this.thickness = thickness;
    }
}