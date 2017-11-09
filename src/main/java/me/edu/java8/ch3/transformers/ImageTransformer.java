package me.edu.java8.ch3.transformers;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

@FunctionalInterface
public interface ImageTransformer {
    Color apply(Image src, int x, int y, Color color);
    
    static ImageTransformer from(PixelsTransformer pt) {
        return (img, x, y, c) -> pt.apply(x, y, img.getPixelReader()); 
    }
}
