package me.edu.java8.ch3;

@FunctionalInterface
public interface ThrowableFunction<T, U> {
    U apply(T t) throws Exception;
}
