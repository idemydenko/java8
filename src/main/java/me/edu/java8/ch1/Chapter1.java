package me.edu.java8.ch1;

import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class Chapter1 {
    public static void main(String[] args) {
//        task1();
        task2("D:/");
    }
    
    public static void task1() {
        final Integer[] array = new Random().ints(10).boxed().toArray(Integer[]::new);
        
        System.out.println("Outer: " + Thread.currentThread().getId());
        
        Arrays.sort(array, (e1, e2) -> {
            System.out.println("Inner: " + Thread.currentThread().getId());
            return Integer.compare(e1, e2);
        });
    }
    
   public static void task2(String path) {
       File root = new File(path);
       
       Stream.of(root.listFiles(File::isDirectory)).map(File::getName).forEach(System.out::println);
   }
   
   public static void task3(String path) {
       File root = new File(path);
       
       Stream.of(root.listFiles(File::isDirectory)).map(File::getName).forEach(System.out::println);
   }

 }
