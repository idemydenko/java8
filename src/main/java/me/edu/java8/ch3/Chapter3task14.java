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
import me.edu.java8.ch3.transformers.PixelsTransformer;

class LatentImage3 {
    
    private Image in;
    private List<PixelsTransformer> operations;

    public static LatentImage3 from(Image in) {
        LatentImage3 result = new LatentImage3();
        result.in = in;
        result.operations = new ArrayList<>();
        return result;
    }

    LatentImage3 transform(UnaryOperator<Color> f) {
        operations.add(PixelsTransformer.from(f));
        return this;
    }


    LatentImage3 transform(PixelsTransformer t) {
        operations.add(t);
        return this;
    }

    public Image toImage() {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                Color c = null;//in.getPixelReader().getColor(x, y);
                for (PixelsTransformer t : operations) {
                    c = t.apply(x, y, in.getPixelReader());
                }
                out.getPixelWriter().setColor(x, y, c);
            }
        return out;
    }
}


public class Chapter3task14 extends Application {
    public static void main(String... args) throws Exception {
        Application.launch(args);
    }

    public void start(Stage stage) {
    	final int frameSize = 10;
        Image image = new Image("eiffel-tower.jpg");
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        Image finalImage = LatentImage3.from(image)
        		.transform((x, y, px) -> px.getColor(width - x - 1, height - y - 1))
                .transform(Color::brighter)
                .transform((x, y, px) -> {           
					if (x > frameSize  && x < image.getWidth() - frameSize && y > frameSize && y < image.getHeight() - frameSize) {
                        return px.getColor(x, y);
                    }
                    return Color.AQUAMARINE;
                })                
                .toImage();
        stage.setScene(new Scene(
                new HBox(new ImageView(image), new ImageView(finalImage))));
        stage.show();
    }
    
}
