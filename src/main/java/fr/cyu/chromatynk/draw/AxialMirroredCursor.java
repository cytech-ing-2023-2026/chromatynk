package fr.cyu.chromatynk.draw;

import fr.cyu.chromatynk.util.Tuple2;
import javafx.scene.canvas.GraphicsContext;

/**
 * A cursor mirrored according to a symmetry axis.
 */
public class AxialMirroredCursor extends DuplicatedCursor {

    private final double lineAX;
    private final double lineAY;
    private final double lineBX;
    private final double lineBY;

    /**
     * Create a new axial-mirrored cursor.
     *
     * @param duplicated the mirrored cursor
     * @param lineAX the starting X of the A point of the axis
     * @param lineAY the starting Y of the A point of the axis
     * @param lineBX the starting X of the B point of the axis
     * @param lineBY the starting Y of the B point of the axis
     *
     */
    public AxialMirroredCursor(Cursor duplicated, double lineAX, double lineAY, double lineBX, double lineBY) {
        super(duplicated);
        this.lineAX = lineAX;
        this.lineAY = lineAY;
        this.lineBX = lineBX;
        this.lineBY = lineBY;
    }

    private Tuple2<Double, Double> getSymmetric(double x1, double y1) {
        if(lineAX == lineBX) return new Tuple2<>(lineAX + (lineAX - x1), y1);

        double slope = (lineBY-lineAY)/(lineBX-lineAX);
        double yIntersection = (lineBX*lineAY-lineAX*lineBY)/(lineBX-lineAX);
        double d = (x1 + (y1 - yIntersection)*slope)/(1 + slope*slope);

        return new Tuple2<>(
                2*d - x1,
                2*d*slope - y1 + 2*yIntersection
        );
    }

    @Override
    public void drawLineAt(GraphicsContext graphics, double x, double y, double dx, double dy) {
        getDuplicated().drawLineAt(graphics, x, y, dx, dy);

        Tuple2<Double, Double> symmetricStart = getSymmetric(x, y);
        Tuple2<Double, Double> symmetricEnd = getSymmetric(dx, dy);

        getDuplicated().drawLineAt(graphics, symmetricStart.a(), symmetricStart.b(), symmetricEnd.a(), symmetricEnd.b());
    }

    @Override
    public void drawAt(GraphicsContext graphics, double x, double y, double dirX, double dirY) {
        getDuplicated().drawAt(graphics, x, y, dirX, dirY);

        Tuple2<Double, Double> symmetricPos = getSymmetric(x, y);
        Tuple2<Double, Double> symmetricDir = getSymmetric(dirX, dirY);

        double length = Math.sqrt(symmetricDir.a()*symmetricDir.a() + symmetricDir.b()*symmetricDir.b());

        getDuplicated().drawAt(graphics, symmetricPos.a(), symmetricPos.b(), symmetricDir.a()/length, symmetricDir.b()/length);
    }
}
