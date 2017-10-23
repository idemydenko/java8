package me.edu.java8.ch3;

import javafx.scene.paint.Color;

@FunctionalInterface
public interface ColorTransformer {
    Color apply(int x, int y, Color color);
    
    // Task 11
    default ColorTransformer compose(C)
}
