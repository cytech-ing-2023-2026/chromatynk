package fr.cyu.chromatynk.draw;

import javafx.scene.canvas.GraphicsContext;

public class MimickedCursor extends DuplicatedCursor {

    private final double translateX;
    private final double translateY;

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
}
