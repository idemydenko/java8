package me.edu.java8.ch3;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Chapter3task6 extends Application {

    public static void main(String... args) throws Exception {
        Application.launch(args);        
    }

    public static <T> Image transform(Image in, BiFunction<T, Color, Color> f, T arg) {
        final int width = (int) in.getWidth();
        final int height = (int) in.getHeight();

        final WritableImage out = new WritableImage(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Color color = in.getPixelReader().getColor(x, y);
                out.getPixelWriter().setColor(x, y, f.apply(arg, color));
            }
        }
        return out;
    }

    
    public void start(Stage stage) throws Exception {
        final Image srcImg = new Image("queen-mary.png");
               
        final Image resImg = transform(srcImg, (f, c) -> c.deriveColor(0, 1, f, 1), 2.5);

        
        final Stream<ImageView> views = Stream.of(srcImg, resImg).map(ImageView::new);
        stage.setScene(new Scene(new HBox(views.toArray(ImageView[]::new))));
        stage.show();
    } 

}
