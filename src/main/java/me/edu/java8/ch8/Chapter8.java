package me.edu.java8.ch8;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
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
	
	static void task6() throws IOException {
		Point2D p1 = new Point2D(1, 1);
		Point2D p2 = new Point2D(2, 2);
		Point2D p3 = new Point2D(3, 3);
		
		Rectangle2D r1 = new Rectangle2D(p1, 1, 1);
		Rectangle2D r2 = new Rectangle2D(p2, 2, 2);
		Rectangle2D r3 = new Rectangle2D(null, 3, 3);
		
		
		List<Point2D> points = new ArrayList<>(Arrays.asList(p3, p1, p2));
		Collections.sort(points, Point2D.CMP);
		System.out.println("Points");
		System.out.println(points.get(0).equals(p1));
		System.out.println(points.get(1).equals(p2));
		System.out.println(points.get(2).equals(p3));
		
		List<Rectangle2D> rectangles = new ArrayList<>(Arrays.asList(r3, r1, r2));
		Collections.sort(rectangles, Rectangle2D.CMP);
		System.out.println("Rectangles");
		System.out.println(rectangles.get(0).equals(r1));
		System.out.println(rectangles.get(1).equals(r2));
		System.out.println(rectangles.get(2).equals(r3));		
	}
	
	static void task7() throws IOException {
		Comparator<Integer> c1 = Comparator.nullsFirst(Comparator.naturalOrder());  
		Comparator<Integer> c2 = Comparator.nullsLast(Comparator.reverseOrder());
		
		Integer[] ints = new Integer[] {null, 2, 1, 0};
		
		Integer[] arr1 = ints.clone();
		Arrays.sort(arr1, c1.reversed());
		
		Integer[] arr2 = ints.clone();
		Arrays.sort(arr2, c2);
		
		System.out.println(Arrays.equals(arr1, arr2));
		System.out.println("A1: " + Arrays.toString(arr1));
		System.out.println("A2: " + Arrays.toString(arr2));
	}
	
	static void task8() throws IOException {
		Queue<Integer> queue = new LinkedList<>();
		queue.add(31);
		putString(queue);
		System.out.println(queue);
		
		queue = new LinkedList<>();
		Queue<Integer> checked =  Collections.checkedQueue(queue, Integer.class);
		checked.add(31);
		try {
			putString(checked);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	static void putString(Queue queue) {
		queue.add("Integer");
	}
	
	static void task10() throws IOException {
		Path src = Paths.get(System.getenv("JAVA_HOME"), "src.zip", "/");
		
		try(FileSystem fs = FileSystems.newFileSystem(src, null)) {
			fs.getRootDirectories().forEach((dir) -> {
				try {
					try (Stream<Path> paths = Files.walk(dir)) {
						paths.filter(Files::isRegularFile).forEach((Path file) -> {
							try {
								String content = new String(Files.readAllBytes(file));
								boolean found = Stream.of(content.split("[\\P{L}]+")).anyMatch((w) -> "volatile".equals(w) || "transient".equals(w));
								if (found) {
									System.out.println(new StringBuilder(file.toString()).substring(1).replaceAll("/", "."));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	static void task11() throws IOException {
		String username = "Admin";
		String password = "Superuser";
		
		String original = username + ":" + password;
		
		String encoded = Base64.getEncoder().encodeToString(original.getBytes());		
		System.out.println(encoded);
		String decoded = new String(Base64.getDecoder().decode(encoded));
		System.out.println(decoded);
	}
	
	static void task14() throws IOException {
		AtomicInteger c = new AtomicInteger(0);
		
		Object o = new Object();
		
		Objects.requireNonNull(o, "Step " + (c.incrementAndGet()));
		Objects.requireNonNull(o, "Step " + (c.incrementAndGet()));
		Objects.requireNonNull(o, "Step " + (c.incrementAndGet()));
		Objects.requireNonNull(o, "Step " + (c.incrementAndGet()));

		System.out.println(c);
		
		Objects.requireNonNull(o, () -> "St " + c.incrementAndGet());
		Objects.requireNonNull(o, () -> "St " + c.incrementAndGet());
		Objects.requireNonNull(o, () -> "St " + c.incrementAndGet());
		Objects.requireNonNull(o, () -> "St " + c.incrementAndGet());
		
		System.out.println(c);
		
		try {
			Objects.requireNonNull(null, () -> "St " + c.incrementAndGet());
		} catch (Exception e) {}
		System.out.println(c);
	}
	
	public static void main(String[] args) throws Exception {
		task14();
	}

}
