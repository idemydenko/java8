package me.edu.java8.ch3;

import java.util.*;
import java.util.function.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import me.edu.java8.ch3.transformers.ColorTransformer;

class LatentImage {
    private Image in;
    private List<ColorTransformer> pendingOperations;

    public static LatentImage from(Image in) {
        LatentImage result = new LatentImage();
        result.in = in;
        result.pendingOperations = new ArrayList<>();
        return result;
    }

    LatentImage transform(UnaryOperator<Color> f) {
        pendingOperations.add(ColorTransformer.from(f));
        return this;
    }

    LatentImage transform(ColorTransformer t) {
        pendingOperations.add(t);
        return this;
    }

    
    public Image toImage() {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                Color c = in.getPixelReader().getColor(x, y);
                
                for (ColorTransformer t : pendingOperations) {
                    c = t.apply(x, y, c);
                }
                
                out.getPixelWriter().setColor(x, y, c);
            }
        return out;
    }
}

public class Chapter3task12 extends Application {

    public static void main(String... args) throws Exception {
        Application.launch(args);
    }

    public void start(Stage stage) {
        Image image = new Image("eiffel-tower.jpg");
        int frameSize = 10;
        
        Image finalImage = LatentImage.from(image).transform(Color::brighter)
                .transform(Color::grayscale)
                .transform((x, y, c) -> {           
                    if (x > frameSize && x < image.getWidth() - frameSize && y > frameSize && y < image.getHeight() - frameSize) {
                        return c;
                    }
                    return Color.AQUAMARINE;
                })
                .toImage();
        stage.setScene(new Scene(
                new HBox(new ImageView(image), new ImageView(finalImage))));
        stage.show();
    }
}
