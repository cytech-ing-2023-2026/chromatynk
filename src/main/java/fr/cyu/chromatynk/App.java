package fr.cyu.chromatynk;

import fr.cyu.chromatynk.draw.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {

        var label = new Label("Hello, JavaFX");
        var canvas = new Canvas(500, 500);
        var scene = new Scene(new StackPane(label, canvas), 640, 480);
        var graphics = canvas.getGraphicsContext2D();
        graphics.setFill(javafx.scene.paint.Color.WHITE);
        graphics.fillRect(0, 0, 500, 500);

        var cursor = new TangibleCursor(100, 100, 0, 0, new Color(1, 0, 0), 1, 5);
        var dupli = new CentralMirroredCursor(cursor, 250, 250);
        dupli.move(graphics, 100, 100);
        dupli.setColor(new Color(0, 1, 0));
        dupli.setThickness(1);
        dupli.move(graphics, 100, -100);
        cursor.move(graphics, 100, 100);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}