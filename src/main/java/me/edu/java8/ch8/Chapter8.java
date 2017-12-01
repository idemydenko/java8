package me.edu.java8.ch8;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Chapter8 {

	static void task1() {
		int i1 = 100;
		int i2 = Integer.MAX_VALUE;
		
		System.out.println("Add");
		System.out.println(i1 + i2);
		System.out.println(Integer.toUnsignedLong(i1 + i2));
		
		System.out.println("Subtract");
		System.out.println(i1 + i2 - 7);
		System.out.println(Integer.toUnsignedLong(i1 + i2 - 7));
		
		System.out.println("Devides");
		System.out.println((i1 + i2 - 7) / 2);
		System.out.println(Integer.toUnsignedLong(i1 + i2 - 7) / Integer.toUnsignedLong(2));
		System.out.println(Integer.divideUnsigned((i1 + i2 - 7), 2));

		System.out.println("Compare");
		System.out.println(Integer.valueOf(i1 + i2).compareTo(Integer.MAX_VALUE));
		System.out.println(Integer.compareUnsigned(i1 + i2, Integer.MAX_VALUE));
		
		System.out.println("Reminder");
		System.out.println((i1 + i2) % 17);
		System.out.println(Integer.remainderUnsigned(i1 + i2, 17));

	}
	
	static void task2() {
		Math.negateExact(Integer.MIN_VALUE);
	}
	
	static void task3() {
		int a = 12;
		int b = 30;
		
		gcb(a, b);
		
		a = -12;
		b = 30;

		gcb(a, b);

		a = 12;
		b = -30;

		gcb(a, b);

		a = -12;
		b = -30;

		gcb(a, b);

	}

	private static void gcb(int a, int b) {
		int res = 0;
		System.out.println("{a, b} = " + String.join(", ", String.valueOf(a), String.valueOf(b)));
		res = gcdEuclid(a, b, (i1, i2) -> i1 % i2);
		System.out.println("a % b: " + res);
		
		res = gcdEuclid(a, b, (i1, i2) -> Math.abs(i1) % Math.abs(i2));
		System.out.println("abs(a) % abs(b): " + res);
		
		res = gcdEuclid(a, b, Math::floorMod);
		System.out.println("Math::floorMod: " + res);
		System.out.println();
	}
	
	static int gcdEuclid(int a, int b, IntBinaryOperator rem) {
		return b == 0 ? a : gcdEuclid(b, rem.applyAsInt(a, b), rem); 
	}
	
	static void task4() {
		final long m = 25214903917L;
		long seed = Stream.iterate(0L, (d) -> prev(d)).limit(1_000_000).min((x, y) -> Long.compare(x ^ m, y ^ m)).get();
		System.out.println(seed);
		
		Random r = new Random(seed ^ m);
		
		double zero = DoubleStream.generate(() -> r.nextDouble()).limit(376_050).min().getAsDouble();
		System.out.println(zero);
		
		System.out.println();
		
		Random r2 = new Random(seed ^ m);
		int index = 0;
		do {
			zero = r2.nextDouble();
			++index;
		} while (zero != 0 && index != 376_050);
		System.out.println(zero);
		System.out.println(index);
	}
	
	static long prev(long seed) {
		final long a = 11;
		final long v = 246154705703781L;
		final long n = 1 << 48;

		return (BigInteger.valueOf(seed).subtract(BigInteger.valueOf(a)))
				.multiply(BigInteger.valueOf(v))
				.mod(BigInteger.valueOf(n)).longValue();
	}
	
	static void task5() throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get("res", "alice.txt"));
		String content = new String(bytes, StandardCharsets.UTF_8);
		String[] words = content.split("[\\P{L}]+");
		
		System.out.println(words.length);
		
		Predicate<String> longWord = (s) -> s.length() > 12;
		System.out.println(Stream.of(words).filter(longWord).count());
		
		List<String> wordsList = new ArrayList<>(Arrays.asList(words));
		wordsList.removeIf((s) -> !longWord.test(s));
		System.out.println(wordsList.size());
	}
	
	public static void main(String[] args) throws Exception {
		task5();
	}

}
