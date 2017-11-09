package me.edu.java8.ch3;

import java.util.function.Function;

public class Pair<T> {
    private final T first;
    private final T second;
    
    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public <U> Pair<U> map(Function<T, U> mapper) {
        return new Pair<U>(mapper.apply(first), mapper.apply(second));
    }
    
    @Override
    public String toString() {
        return "Pair [first=" + first + ", second=" + second + "]";
    }
    
}
