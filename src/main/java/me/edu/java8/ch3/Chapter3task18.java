package me.edu.java8.ch3;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class Chapter3task18 {
    public static <T> Supplier<T> unchecked(Callable<T> f) {
        return () -> {
            try {
                return f.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } catch (Throwable t) {
                throw t;
            }
        };
    }
   
    public static <T, U> Function<T, U> unchecked(ThrowableFunction<T, U> f) {
        return (t) -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } catch (Throwable tr) {
                throw tr;
            }
        };
    }

    
    public static void main(String[] args) {
        Supplier<String> s = unchecked(
                () -> new String(Files.readAllBytes(Paths.get("/etc/passwd")),
                        StandardCharsets.UTF_8));

        Function<String, String> f = unchecked((p) -> new String(Files.readAllBytes(Paths.get(p)))); 
    }

    
}
