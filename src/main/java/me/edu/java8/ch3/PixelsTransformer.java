package me.edu.java8.ch3;

import java.util.function.UnaryOperator;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

@FunctionalInterface
public interface PixelsTransformer {
	Color apply(int x, int y, PixelReader reader);
	
	static PixelsTransformer from(UnaryOperator<Color> op) {
		return (x, y, r) -> op.apply(r.getColor(x, y));
	}
	
	static PixelsTransformer from(ColorTransformer op) {
		return (x, y, r) -> op.apply(x, y, r.getColor(x, y));
	}
}
