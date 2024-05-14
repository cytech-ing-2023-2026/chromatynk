package fr.cyu.chromatynk.eval;

public class Canvas {

    //Could have been a record but decided to make a POJO to explicitly tell this class is mutable (because arrays are).
    private final int width;
    private final int height;
    private final Color[][] pixels;

    public Canvas(int width, int height, Color[][] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public Canvas(int width, int height) {
        this(width, height, new Color[height][width]);
        fill(new Color(1, 1, 1));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color[][] getPixels() {
        return pixels;
    }

    public boolean contains(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void drawPixel(int x, int y, Color color) {
        if (contains(x, y)) pixels[y][x] = color;
    }

    public void drawLine(int startX, int startY, int endX, int endY, Color color) {
        int dx = Math.abs(endX - startX);
        int sx = startX < endX ? 1 : -1;

        int dy = -Math.abs(endY - startY);
        int sy = startY < endY ? 1 : -1;

        int err = dx + dy, e2;

        int x = startX;
        int y = startY;

        while (x != endX || y != endY) {
            drawPixel(x, y, color);

            e2 = 2 * err;

            if (e2 > dy) {
                err += dy;
                x += sx;
            }

            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }

        drawPixel(x, y, color);
    }

    public void fill(Color color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) pixels[y][x] = color;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) result.append(pixels[y][x].toANSI()).append(' ');
            result.append("\u001B[0m\n");
        }
        return result.substring(0, result.length() - 1);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Canvas canvas)) return false;

        if(width != canvas.getWidth() || height != canvas.getHeight()) return false;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixels[y][x] != canvas.pixels[y][x]) return false;
            }
        }

        return true;
    }
}
