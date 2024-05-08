package fr.cyu.chromatynk.eval;

import javafx.scene.paint.Color;

public record Canvas(Color[][] pixels, int width, int height) {

    public Canvas withLine(int x, int y, int width, int height) {
        //TODO
        return null;
    }
}
