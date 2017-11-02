package me.edu.java8.ch3;


import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

class LatentImage2 {
    
    static class Op {
        final boolean now;
        final ColorsTransformer ct;
        
        public Op(ColorsTransformer ct, boolean now) {
            this.ct = ct;
            this.now = now;
        }

        public Op(ColorsTransformer ct) {
            this(ct, false);
        }
        
        public ColorsTransformer ct() {
            return ct;
        }
        
        public boolean now() {
            return now;
        }
    }
    
    private Image in;
    private List<ColorTransformer> pendingOperations;

    public static LatentImage2 from(Image in) {
        LatentImage2 result = new LatentImage2();
        result.in = in;
        result.pendingOperations = new ArrayList<>();
        return result;
    }

    LatentImage2 transform(UnaryOperator<Color> f) {
        pendingOperations.add(ColorTransformer.from(f));
        return this;
    }

    LatentImage2 transform(ColorTransformer t) {
        pendingOperations.add(t);
        return this;
    }

    LatentImage2 transform(ColorsTransformer ct) {
        Image img = applyPendings();
        transform0(ct, img);
        return this;
    }

    private void transform0(ColorsTransformer ct, Image i mage) {
        
    }

    private Image applyPendings() {
        final Image result = toImage();
        pendingOperations.clear();
        return result;
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


public class Chapter3task13 extends Application {
    public static void main(String... args) throws Exception {
        Application.launch(args);
    }

    public void start(Stage stage) {
        Image image = new Image("eiffel-tower.jpg");
        int frameSize = 10;
        
        Image finalImage = LatentImage2.from(image).transform(Color::brighter)
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
