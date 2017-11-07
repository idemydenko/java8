package me.edu.java8.ch3;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

@FunctionalInterface
public interface ColorsTransformer {
    Color apply(Image src, int x, int y, Color color);    
}
