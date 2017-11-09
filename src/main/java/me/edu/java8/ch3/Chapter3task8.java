package me.edu.java8.ch3;

import java.util.stream.Stream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import me.edu.java8.ch3.transformers.ColorTransformer;

public class Chapter3task8 extends Application {
    public static void main(String... args) throws Exception {
        Application.launch(args);        
    }

    public static Image transform(Image in, int frameSize) {
        final int width = (int) in.getWidth();
        final int height = (int) in.getHeight();

        ColorTransformer f = transformer(width, height, frameSize);
        final WritableImage out = new WritableImage(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Color color = in.getPixelReader().getColor(x, y);
                out.getPixelWriter().setColor(x, y, f.apply(x, y, color));
            }
        }
        return out;
    }

    public static ColorTransformer transformer(int width, int height, int frameSize) {
        return (x, y, c) -> {           
            if (x > frameSize && x < width - frameSize && y > frameSize && y < height - frameSize) {
                return c;
            }
            return Color.GRAY;
         };
    }

    public void start(Stage stage) throws Exception {
        final int frameSize = 40;
        final Image srcImg = new Image("queen-mary.png");
               
        final Image resImg = transform(srcImg, frameSize);

        
        final Stream<ImageView> views = Stream.of(srcImg, resImg).map(ImageView::new);
        stage.setScene(new Scene(new HBox(views.toArray(ImageView[]::new))));
        stage.show();
    } 

}
