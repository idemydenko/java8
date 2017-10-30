package me.edu.java8.ch3;

import java.util.function.UnaryOperator;

import javafx.scene.paint.Color;

@FunctionalInterface
public interface ColorTransformer {
    Color apply(int x, int y, Color color);
    
    // Task 11
    static ColorTransformer compose(ColorTransformer ct1, ColorTransformer ct2) {    	
    	return (x, y, color) -> ct2.apply(x, y, ct1.apply(x, y, color));
    }
    
    static ColorTransformer from(UnaryOperator<Color> op) {
    	return (x, y, color) -> op.apply(color);
    }
}
