package fr.cyu.chromatynk.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A tangible/real cursor.
 */
public class TangibleCursor implements Cursor {

    private double x;
    private double y;
    private double dirX;
    private double dirY;
    private boolean visible;
    private Color color;
    private double opacity;
    private double thickness;

    /**
     * Create a new tangible cursor.
     *
     * @param x the X coordinate of this cursor
     * @param y the Y coordinate of this cursor
     * @param dirX the X direction of this cursor
     * @param dirY the Y direction of this cursor
     * @param visible the visibility of this cursor
     * @param color the color to use when drawing with this cursor
     * @param opacity the opacity to use when drawing with this cursor
     * @param thickness the thickness to use when drawing with this cursor
     */
    public TangibleCursor(double x, double y, double dirX, double dirY, boolean visible, Color color, double opacity, double thickness) {
        this.x = x;
        this.y = y;
        this.dirX = dirX;
        this.dirY = dirY;
        this.visible = visible;
        this.color = color;
        this.opacity = opacity;
        this.thickness = thickness;
    }

    /**
     * Create a new tangible cursor with default values.
     *
     * @param x the X coordinate of this cursor
     * @param y the Y coordinate of this cursor
     */
    public TangibleCursor(double x, double y) {
        this(x, y, 1, 0, true, new Color(0, 0, 0), 1, 1);
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
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
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

    @Override
    public void drawLineAt(GraphicsContext graphics, double x, double y, double dx, double dy) {
        graphics.setStroke(new javafx.scene.paint.Color(getColor().red(), getColor().green(), getColor().blue(), opacity));
        graphics.setLineWidth(getThickness());
        graphics.strokeLine(x, y, dx, dy);
    }
}