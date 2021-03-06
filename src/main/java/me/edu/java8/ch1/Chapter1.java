package me.edu.java8.ch1;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Chapter1 {
	public static void main(String[] args) {
		// task1();
		// task2("D:/");
		// task3("D:/Temp");
		// task4("D:/Temp");
//		task6();
//		task7();
//	    task8();
	    task9();
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

		Stream.of(root.listFiles((f, n) -> new File(f, n).isFile() && n.endsWith(".pdf"))).map(File::getName)
				.forEach(System.out::println);
	}

	public static void task4(String path) {
		File root = new File(path);

		Stream.of(root.listFiles()).sorted((f1, f2) -> {
			if (f1.isFile() && f2.isDirectory()) {
				return -1;
			}

			if (f1.isDirectory() && f2.isFile()) {
				return 1;
			}
			return -f1.getName().compareToIgnoreCase(f2.getName());
		}).map(File::getName).forEach(System.out::println);
	}

	public static void task6() {
		new Thread(RunnableEx.unchecked(() -> {
			System.out.println("Zzz...!");
			Thread.sleep(1000);
			System.out.println("zzZzzZ...!");
		})).start();
	}

	public static void task7() {
		RunnableMerge.andThan(() -> System.out.println("First run"), () -> System.out.println("Second run"));
	}
	
	
	public static void task8() {
	    String[] names = { "Peter", "Paul", "Mary" };
	    
	    List<Runnable> runners = new ArrayList<>();
	    
	    for (String name : names) {
            runners.add(() -> System.out.println(name));
        }

//        for (int i = 0; i < names.length; i++) {
//            runners.add(() -> System.out.println(names[i]));
//        }

	    for (Runnable runnable : runners) {
            new Thread(runnable).start();
        }
	}
	
	public static void task9() {
//	    Collection2<Integer> collection = new ArrayList2<>();
//	    
//	    IntStream.range(-5, 5).forEach(collection::add);
//	    
//	    collection.forEachIf(System.out::println, (e) -> e > 2);
	}
}
