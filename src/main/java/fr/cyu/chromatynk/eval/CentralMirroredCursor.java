package fr.cyu.chromatynk.eval;

public class CentralMirroredCursor implements Cursor {

    private final Cursor mirroring;
    private final Cursor mirrored;
    private final double centerX;
    private final double centerY;

    public CentralMirroredCursor(Cursor mirrored, double centerX, double centerY) {
        this.mirroring = mirrored.copyTangible();
        this.mirroring.setX(centerX + (centerX - mirroring.getX()));
        this.mirroring.setY(centerY + (centerY - mirroring.getY()));
        this.mirroring.setDirX(-mirrored.getDirX());
        this.mirroring.setDirY(-mirrored.getDirY());
        
        this.mirrored = mirrored;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public double getX() {
        return mirrored.getX();
    }

    @Override
    public double getY() {
        return mirrored.getY();
    }

    @Override
    public void setX(double x) {
        mirrored.setX(x);
    }

    @Override
    public void setY(double y) {
        mirrored.setY(y);
    }

    @Override
    public double getDirX() {
        return mirrored.getDirX();
    }

    @Override
    public void setDirX(double dirX) {
        mirrored.setDirX(dirX);
    }

    @Override
    public double getDirY() {
        return mirrored.getDirY();
    }

    @Override
    public void setDirY(double dirY) {
        mirrored.setDirY(dirY);
    }

    @Override
    public Color getColor() {
        return mirrored.getColor();
    }

    @Override
    public void setColor(Color color) {
        mirrored.setColor(color);
    }

    @Override
    public double getOpacity() {
        return mirrored.getOpacity();
    }

    @Override
    public void setOpacity(double opacity) {
        mirrored.setOpacity(opacity);
    }

    @Override
    public double getThickness() {
        return mirrored.getThickness();
    }

    @Override
    public void setThickness(double thickness) {
        mirrored.setThickness(thickness);
    }
}
