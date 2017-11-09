package me.edu.java8.ch3;

public class Chapter3task23 {

    public static void main(String[] args) {
        Pair<Integer> p1 = new Pair<Integer>(10, 100);
        Pair<String> p2 = p1.map(Integer::toBinaryString);
        System.out.println(p2);
    }

}
