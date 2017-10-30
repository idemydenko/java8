package me.edu.java8.ch3;

import java.util.function.UnaryOperator;

import javafx.scene.paint.Color;

@FunctionalInterface
public interface ColorTransformer {
    Color apply(int x, int y, Color color);
    
    // Task 11
    static ColorTransformer compose(ColorTransformer ct1, ColorTransformer ct2) {
    	
    	return (x, y, c) -> {
    		UnaryOperator<Color> op1 = (c1) -> ct1.apply(x, y, c1);
    		UnaryOperator<Color> op2 = (c2) -> ct2.apply(x, y, c2);

    		return ct2.apply(x, y, op1.andThen(op2).apply(c));
    	};
    }
    
    static ColorTransformer from(UnaryOperator<Color> op) {
    	ColorTransformer ct = (x, y, color) -> op.apply(color); 
    	return ct;
    }
}
