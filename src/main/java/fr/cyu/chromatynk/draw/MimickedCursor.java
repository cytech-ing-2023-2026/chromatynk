package fr.cyu.chromatynk.draw;

import javafx.scene.canvas.GraphicsContext;

/**
 * A mimicked cursor.
 */
public class MimickedCursor extends DuplicatedCursor {

    private final double translateX;
    private final double translateY;

    /**
     * Create new mimicked cursor.
     *
     * @param mimicked the cursor being mimicked
     * @param translateX the X translation from the mimicked cursor to the mimic's position
     * @param translateY the Y translation from the mimicked cursor to the mimic's position
     */
    public MimickedCursor(Cursor mimicked, double translateX, double translateY) {
        super(mimicked);
        this.translateX = translateX;
        this.translateY = translateY;
    }

    @Override
    public void drawLineAt(GraphicsContext graphics, double x, double y, double dx, double dy) {
        getDuplicated().drawLineAt(graphics, x, y, dx, dy);
        getDuplicated().drawLineAt(graphics, x+translateX, y+translateY, translateX+dx, translateY+dy);
    }

    @Override
    public void drawAt(GraphicsContext graphics, boolean current, double x, double y, double dirX, double dirY) {
        getDuplicated().drawAt(graphics, current, x, y, dirX, dirY);
        getDuplicated().drawAt(graphics, current, x+translateX, y+translateY, dirX, dirY);
    }

    /**
     * Mimic a cursor at the given absolute position.
     *
     * @param mimicked the mimicked cursor
     * @param x the X position of the mimic
     * @param y the Y position of the mimic
     * @return a new {@link MimickedCursor} with {@code mimicked} as the cursor being mimicked
     */
    public static MimickedCursor at(Cursor mimicked, double x, double y) {
        return new MimickedCursor(mimicked, x - mimicked.getX(), y - mimicked.getY());
    }
}
