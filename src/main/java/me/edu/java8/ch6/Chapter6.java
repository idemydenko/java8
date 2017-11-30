package me.edu.java8.ch6;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Chapter6 {

	static void task3() throws Exception {	
		final LongAdder adder = new LongAdder();		
		final AtomicLong atomic = new AtomicLong();
		
		
		Collection<? extends Callable<?>> tasks = callables(() -> adder.increment());		
		ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
		
		Instant start = Instant.now();
		executor.invokeAll(tasks);		
		executor.shutdown();
		if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
			throw new IllegalStateException("adder too long");
		}
		Duration elapsed = Duration.between(start, Instant.now());
		System.out.println(adder);
		System.out.println("adder: " + elapsed);

		System.out.println();
		
		tasks = callables(() -> atomic.incrementAndGet());		
		executor = Executors.newFixedThreadPool(tasks.size());
		
		start = Instant.now();
		executor.invokeAll(tasks);		
		executor.shutdown();
		if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
			throw new IllegalStateException("atomic too long");
		}
		elapsed = Duration.between(start, Instant.now());
		System.out.println(atomic);
		System.out.println("atomic: " + elapsed);

		
	}

	static Collection<? extends Callable<?>> callables(Runnable action) {
		final int threads = 1000;
		final int times = 100000;

		Runnable r = () -> IntStream.range(0, times).forEach((i) -> action.run());		
		return IntStream.range(0, threads).mapToObj((i) -> Executors.callable(r)).collect(Collectors.toList());
	}
	
	static void task4() throws Exception {
		LongAccumulator length = new LongAccumulator(Math::max, 0);
		String content = new String(Files.readAllBytes(Paths.get("res", "alice.txt")), StandardCharsets.UTF_8);
		
		Stream.of(content.split("[\\P{L}]+")).forEach((s) -> length.accumulate(s.length()));
		System.out.println(length);
	}

	static void task5() throws Exception {
		final ConcurrentHashMap<String, Set<File>> stat = new ConcurrentHashMap<>(100000);
		
		Files.list(Paths.get("res")).forEach((p) -> readFile(p, (f, w) -> {
			final Set<File> set = new HashSet<>();
			set.add(f);
			stat.merge(w, set, (s1, s2) -> {
				Set<File> result = new HashSet<>(s1);
				result.addAll(s2);
				return result;
			});
		}));
		
		System.out.println(stat.size());
		System.out.println("Search:" + stat.searchValues(Integer.MAX_VALUE, (f) -> f.size() > 3 ? f : null));
		System.out.println("Reduce:" + stat.reduceValues(Integer.MAX_VALUE, (f) -> f.size() > 3 ? 1 : null , Math::addExact));
		stat.forEach(Integer.MAX_VALUE, (k, v) -> v.size() > 3 ? k : null , System.out::println);
	}

	static void readFile(Path path, BiConsumer<File, String> biConsumer) {		
		Callable<?> callable = () -> { 
			String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
			Stream.of(content.split("[\\P{L}]+")).forEach((w) -> biConsumer.accept(path.toFile(), w));
			return null;
		};
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		Future<?> future = executor.submit(callable);
		try {
			future.get();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		
		executor.shutdown();
	}

	static void task6() throws Exception {
		final ConcurrentHashMap<String, Set<File>> stat = new ConcurrentHashMap<>(100000);
		
		Files.list(Paths.get("res")).forEach((p) -> readFile(p, (f, w) -> stat.computeIfAbsent(w, (s) -> new HashSet<>()).add(f)));
		
		System.out.println(stat.size());
		System.out.println("Search:" + stat.searchValues(Integer.MAX_VALUE, (f) -> f.size() > 3 ? f : null));
		System.out.println("Reduce:" + stat.reduceValues(Integer.MAX_VALUE, (f) -> f.size() > 3 ? 1 : null , Integer::sum));
		stat.forEach(Integer.MAX_VALUE, (k, v) -> v.size() > 3 ? k : null , System.out::println);
	}

	static void task7() throws Exception {
		final ConcurrentHashMap<String, Long> stat = new ConcurrentHashMap<>(50000);
		String content = new String(Files.readAllBytes(Paths.get("res", "war-and-peace.txt")), StandardCharsets.UTF_8);
		
		Stream.of(content.split("[\\P{L}]+")).forEach((s) -> stat.put(s, Integer.valueOf(s.length()).longValue()));
		
		Entry<String, Long> res = stat.reduceEntries(Integer.MAX_VALUE, (e1, e2) -> e1.getValue() > e2.getValue() ? e1 : e2);
		System.out.println(res);
		
	}
	
	static void task9() {
		
		int n = 40;
		
		BSMatrix[] array = new BSMatrix[n];
		
		Arrays.parallelSetAll(array, (i) -> BSMatrix.one());
		Arrays.parallelPrefix(array, (m1, m2) -> m1.mult(m2));
		System.out.println(Arrays.toString(array));
		
	}

	static void task10() throws Exception {
		String url = "file:///D:/Doc/java8/api/java/nio/file/Files.html";
	}
	
	static void print(String url) {
		Supplier<String> loader = () -> {
			try {
				return new String(Files.readAllBytes(Paths.get(URI.create(url))), StandardCharsets.UTF_8);
			} catch (Exception e) {
				throw new IllegalStateException("loading error");
			}
		};
		
//		CompletableFuture.supplyAsync(loader).thenCompose((s) -> s.split("[\\P{L}]+").);
	}
	
	public static void main(String[] args) throws Exception {
		task9();
	}

}
