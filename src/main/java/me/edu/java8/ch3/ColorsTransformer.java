package me.edu.java8.ch3;

import java.util.function.UnaryOperator;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public interface ColorsTransformer {
    Color apply(Image src, int x, int y, Color color);
    
    static ColorsTransformer from(ColorTransformer ct) {
        return (img, x, y, color) -> ct.apply(x, y, color);
    }
    
    static ColorsTransformer from(UnaryOperator<Color> op) {
        return (img, x, y, color) -> op.apply(color);
    }

}
