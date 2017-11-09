package me.edu.java8.ch3;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Chapter3task16 {
    public static <T> void doInOrderAsync(Supplier<T> first, BiConsumer<T, Throwable> handler) {
        Thread t = new Thread() {
              public void run() {
                 T result = null;
                 try {
                    result = first.get();
                    handler.accept(result, null);
                 } catch (Throwable t) {                   
                    handler.accept(result, t);
                 }
              }
           };
        t.start();  
     }

     public static void main(String[] args) {
        doInOrderAsync(
           () -> args[0],
           (r, e) -> {
               Optional.ofNullable(e).ifPresent((t) -> System.out.println("Yikes: " + t));
               System.out.println("Result: " + r);
           }             
        );      

        doInOrderAsync(
                () -> "Blah",
                (r, e) -> {
                    Optional.ofNullable(e).ifPresent((t) -> System.out.println("Yikes: " + t));
                    System.out.println("Result: " + r);
                }             
             );      

     }
}
