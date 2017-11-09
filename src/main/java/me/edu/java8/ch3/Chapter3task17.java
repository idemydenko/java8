package me.edu.java8.ch3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Chapter3task17 {
    public static void doInOrder(Runnable first, Runnable second, Consumer<Throwable> handler) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(handle(first, handler));
        executor.execute(handle(second, handler));
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
     }

     public static void main(String[] args) throws Exception {
        doInOrder(
           () -> System.out.println(args[0]),
           () -> System.out.println("Goodbye"),
           (e) -> System.out.println("Yikes: " + e));      
        
     }

     static Runnable handle(Runnable r, Consumer<Throwable> handler) {
         return () -> {
             try {
                 r.run();
             } catch (Throwable t) {
                 handler.accept(t);
             }
         };
     }
}
