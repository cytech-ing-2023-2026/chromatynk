package fr.cyu.chromatynk.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A cursor/pencil.
 */
public interface Cursor {

    /**
     * Get the X coordinate of this cursor.
     */
    double getX();

    /**
     * Get the Y coordinate of this cursor.
     */
    double getY();

    /**
     * Set the X position of this cursor.
     *
     * @param x the new X coordinate of this cursor
     */
    void setX(double x);

    /**
     * Set the Y position of this cursor.
     *
     * @param y the new Y coordinate of this cursor
     */
    void setY(double y);

    /**
     * Get the X direction of this cursor.
     */
    double getDirX();

    /**
     * Set the X direction of this cursor.
     *
     * @param dirX the new X coordinate of this cursor's direction
     */
    void setDirX(double dirX);

    /**
     * Get the Y direction of this cursor.
     */
    double getDirY();

    /**
     * Set the Y direction of this cursor.
     *
     * @param dirY the new Y coordinate of this cursor's direction
     */
    void setDirY(double dirY);

    /**
     * Check if this cursor is visible.
     */
    boolean isVisible();

    /**
     * Set the visibility of this cursor.
     */
    void setVisible(boolean visible);

    /**
     * Get drawing color of this cursor.
     */
    Color getColor();

    /**
     * Set the drawing color of this cursor.
     *
     * @param color the new color used when drawing with this cursor
     */
    void setColor(Color color);

    /**
     * Get drawing opacity of this cursor.
     */
    double getOpacity();

    /**
     * Set the drawing opacity of this cursor.
     *
     * @param opacity the new opacity used when drawing with this cursor
     */
    void setOpacity(double opacity);

    /**
     * Get drawing thickness of this cursor.
     */
    double getThickness();

    /**
     * Set the drawing thickness of this cursor.
     *
     * @param thickness the new opacity used when drawing with this cursor
     */
    void setThickness(double thickness);

    /**
     * Draw a line with this cursor.
     *
     * @param graphics the graphics context to draw on
     * @param x the starting X coordinate of the line
     * @param y the starting Y coordinate of the line
     * @param dx the ending X coordinate of the line
     * @param dy the ending Y coordinate of the line
     */
    void drawLineAt(GraphicsContext graphics, double x, double y, double dx, double dy);

    /**
     * Draw this cursor.
     *
     * @param graphics the graphics context to draw on
     * @param x the X coordinate of the rendered cursor
     * @param y the Y coordinate of the rendered cursor
     * @param dirX the direction X of the rendered cursor
     * @param dirY the direction Y of the rendered cursor
     */
    void drawAt(GraphicsContext graphics, double x, double y, double dirX, double dirY);

    /**
     * Move this cursor to another position, tracing a line between the starting and ending positions.
     *
     * @param graphics the graphics context to draw on
     * @param dx the ending relative X coordinate of the line
     * @param dy the ending relative Y coordinate of the line
     */
    default void move(GraphicsContext graphics, double dx, double dy) {
        drawLineAt(graphics, getX(), getY(), getX()+dx, getY()+dy);
        setX(getX()+dx);
        setY(getY()+dy);
    }

    /**
     * Move this cursor on the given distance, following its current direction and tracing a line between the starting and ending positions.
     *
     * @param graphics the graphics context to draw on
     * @param distance the distance to travel
     */
    default void move(GraphicsContext graphics, double distance) {
        move(graphics, distance*getDirX(), distance*getDirY());
    }

    /**
     * Rotate this cursor according to the given angle.
     *
     * @param degrees the rotation angle in degrees in clock direction
     */
    default void turn(double degrees) {
        double currentAngleRad = Math.atan2(getDirY(), getDirX());
        double finalAngle = currentAngleRad + Math.toRadians(degrees);

        setDirX(Math.cos(finalAngle));
        setDirY(Math.sin(finalAngle));
    }

    /**
     * Copy this cursor to a new tangible one.
     *
     * @return a new {@link TangibleCursor} with a snapshot of this cursor's data
     */
    default Cursor copyTangible() {
        return new TangibleCursor(getX(), getY(), getDirX(), getDirY(), isVisible(), getColor(), getOpacity(), getThickness());
    }
}
