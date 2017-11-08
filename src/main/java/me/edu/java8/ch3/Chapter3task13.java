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
        in = transform0(img, ct);
        return this;
    }

    private Image transform0(Image image, ColorsTransformer ct) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                Color c = image.getPixelReader().getColor(x, y);
                c = ct.apply(image, x, y, c);
                out.getPixelWriter().setColor(x, y, c);
            }
        return out;
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
        
        Image finalImage = LatentImage2.from(image)
//                .transform(Color::brighter)                
//                .transform(Color::grayscale)
                .transform(edge())
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
    
    ColorsTransformer blur() {
        return (img, x, y, c) -> {
            int i = x > 0 ? x - 1 : x;
            int j = y > 0 ? y - 1 : y;
            int w = x < img.getWidth() - 1 ? x + 1 : (int) img.getWidth() - 1;  
            int h = y < img.getHeight() - 1 ? y + 1 : (int) img.getHeight() - 1;
            
            double r = 0;
            double g = 0;
            double b = 0;
            int count = (w - i + 1) * (h - j + 1) / 2;
            
            for (; i <= w; i++) {
                for (; j <= h; j++) {
                    Color color = img.getPixelReader().getColor(i, j);
                    r += color.getRed();
                    g += color.getGreen();
                    b += color.getBlue();
                }
            }
            
            return Color.color(r/count, g/count, b/count);
        };
    }
    
    ColorsTransformer edge() {
        return (img, x, y, c) -> {
            Color w = x > 0 ? img.getPixelReader().getColor(x - 1, y) : Color.BLACK;
            Color n = y > 0 ? img.getPixelReader().getColor(x, y - 1) : Color.BLACK;
            Color e = x < img.getWidth() - 1 ? img.getPixelReader().getColor(x + 1, y) : Color.BLACK;  
            Color s = y < img.getHeight() - 1 ? img.getPixelReader().getColor(x, y + 1) : Color.BLACK;
            Color cc = img.getPixelReader().getColor(x, y);
            
            double red = Math.min(1, Math.abs(4 * cc.getRed() - w.getRed() - n.getRed() - e.getRed() - s.getRed()));
            double green = Math.min(1, Math.abs(4 * cc.getGreen() - w.getGreen() - n.getGreen() - e.getGreen() - s.getGreen()));
            double blue = Math.min(1, Math.abs(4 * cc.getBlue() - w.getBlue() - n.getBlue() - e.getBlue() - s.getBlue()));

            return Color.color(red, green, blue);
        };
    }

}
