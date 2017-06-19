package me.edu.java8.ch2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Chapter2 {

    private static final String ALICE_TXT = "res/alice.txt";
    private static final String WAR_AND_PEACE_TXT = "res/war-and-peace.txt";
    private static final int LENGTH = 12;

    public static void main(String[] args) throws Exception {
        task13();
        task12();
    }

    static void task1() throws Exception {
        String contents = new String(Files.readAllBytes(Paths.get(ALICE_TXT)),
                StandardCharsets.UTF_8);
        List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));

        long count = 0;
        for (String w : words) {
            if (w.length() > LENGTH)
                count++;
        }
        System.out.println(count);

        int[] count2 = { 0 };
        for (String w : words) {
            new Thread(() -> {
                if (w.length() > LENGTH) {
                    count2[0]++;
                }
            }).start();
        }
        System.out.println(count2[0]);

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        int[] count3 = { 0 };
        for (String w : words) {
            executorService.execute(() -> {
                if (w.length() > LENGTH) {
                    count3[0]++;
                }
            });
        }

        while (executorService.awaitTermination(1, TimeUnit.SECONDS))
            ;
        System.out.println(count3[0]);

        executorService.shutdown();

    }

    static void task2() throws Exception {
        String contents = new String(Files.readAllBytes(Paths.get(ALICE_TXT)),
                StandardCharsets.UTF_8);
        List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));

        words.stream().filter((w) -> {
            boolean result = w.length() > LENGTH;

            if (result) {
                System.out.println("\nFound");
            } else {
                System.out.print(".");
            }

            return result;
        }).peek((w) -> System.out.println("log: " + w)).limit(5)
                .forEach(System.out::println);
    }

    static void task3() throws Exception {
        String contents = new String(
                Files.readAllBytes(Paths.get(WAR_AND_PEACE_TXT)),
                StandardCharsets.UTF_8);
        List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));

        long start = System.currentTimeMillis();
        long count = words.stream().filter((w) -> w.length() > LENGTH).count();
        System.out.println(
                "sequential took " + (System.currentTimeMillis() - start));
        System.out.println(count);

        start = System.currentTimeMillis();
        count = words.parallelStream().filter((w) -> w.length() > LENGTH)
                .count();
        System.out.println(
                "parallel took " + (System.currentTimeMillis() - start));
        System.out.println(count);

    }

    static void task4() throws Exception {
        int[] values = { 1, 4, 9, 16 };

        Stream<?> stream = Stream.of(values);
        System.out.println(stream.count());

        IntStream stream2 = IntStream.of(values);
        System.out.println(stream2.count());
    }

    static void task5() throws Exception {
        Long seed = 17L;
        Long a = 25214903917L;
        Long c = 11L;
        Long m = 2L << 48;

        System.out.println("m = " + m);

        Stream<Long> rnd = Stream.iterate(seed, (x) -> (a * x + c) % m);

        rnd.limit(20).forEach(System.out::println);
    }

    static void task6() throws Exception {
        String contents = new String(Files.readAllBytes(Paths.get(ALICE_TXT)),
                StandardCharsets.UTF_8);
        List<String> wordList = Arrays.asList(contents.split("[\\P{L}]+"));

        Stream<String> words = wordList.stream();

        Stream<String> song = Stream.of("row", "row", "row", "your", "boat",
                "gently", "down", "the", "stream");
        Stream<Character> letters = song.flatMap(w -> characterStream(w));
        letters.forEach((c) -> System.out.print(c + " "));
    }

    public static Stream<Character> characterStream(String s) {
        return IntStream.range(0, s.length()).mapToObj(s::charAt);
    }

    static void task7() throws Exception {
        Stream<?> stream = null;

        stream = IntStream.range(0, 10).boxed();
        System.out.println("finite: " + isFinite(stream));
        stream.limit(LENGTH);

        stream = IntStream.generate(
                () -> Long.valueOf(System.currentTimeMillis()).intValue())
                .boxed();
        System.out.println("finite: " + isFinite(stream));
        stream.limit(LENGTH);

        stream = IntStream.iterate(0, (x) -> x + 1).boxed();
        System.out.println("finite: " + isFinite(stream));
        stream.limit(LENGTH);
    }

    public static <T> boolean isFinite(Stream<T> stream) {
        Stream<T> stuff = StreamSupport.stream(stream.spliterator(), false);// Stream.concat(stream,
                                                                            // Stream.empty());
        return stuff.collect(Collectors.toList()).size() >= 0;
    }

    static void task8() throws Exception {
        Stream<Integer> a;
        Stream<Integer> b;

        a = IntStream.range(0, 5).boxed();
        b = IntStream.generate(() -> 100).limit(2).boxed();
        StringJoiner sj = new StringJoiner(",", "[", "]");
        zip(a, b).map(Object::toString).forEach(sj::add);
        System.out.println(sj);

        a = IntStream.range(0, 5).boxed();
        b = IntStream.generate(() -> 100).limit(8).boxed();
        sj = new StringJoiner(",", "[", "]");
        zip(a, b).map(Object::toString).forEach(sj::add);
        System.out.println(sj);

        a = IntStream.range(0, 5).boxed();
        b = IntStream.generate(() -> 100).limit(5).boxed();
        sj = new StringJoiner(",", "[", "]");
        zip(a, b).map(Object::toString).forEach(sj::add);
        System.out.println(sj);

    }

    public static <T> Stream<T> zip(Stream<T> a, Stream<T> b) {
        int i = 0;
        Iterator<T> ita = a.iterator();
        Iterator<T> itb = b.iterator();

        Stream.Builder<T> builder = Stream.builder();

        while (ita.hasNext() && itb.hasNext()) {
            builder.accept(i % 2 == 0 ? ita.next() : itb.next());
            i++;
        }

        return builder.build();
    }

    static void task9() throws Exception {
        ArrayList<Integer> a = IntStream.generate(() -> 0).limit(3).boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Integer> b = IntStream.generate(() -> 1).limit(4).boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Integer> c = IntStream.generate(() -> 2).limit(5).boxed()
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> result;

        result = Stream.of(a, b, c)
                .reduce((l1, l2) -> {
                    ArrayList<Integer> l = new ArrayList<>(l1);
                    l.addAll(l2);
                    return l;
                })
                .orElse(new ArrayList<>());
        System.out.println("1st: " + result);

        result = Stream.of(a, b, c)
                .reduce(new ArrayList<>(), (l1, l2) -> {
                    ArrayList<Integer> l = new ArrayList<>(l1);
                    l.addAll(l2);
                    return l;
                });
        System.out.println("2nd: " + result);

        result = Stream.of(a, b, c)
                .reduce(new ArrayList<>(), 
                        (l, e) -> {
                            l.addAll(e);
                            return l;
                        },
                        (l1, l2) -> {
                            ArrayList<Integer> l = new ArrayList<>(l1);
                            l.addAll(l2);
                            return l;                            
                        });
        System.out.println("3rd: " + result);
    }

    static void task10() throws Exception {
//        Random random = new Random();
//        StringJoiner sj = new StringJoiner(", ", "{ ", " }");
//        random.doubles(6, 0, 10).mapToObj(Double::toString).forEach(sj::add);
//        System.out.println("final Double[] array = new Double[] " + sj + ";");
        
        final Double[] array = new Double[] { 4.727435672787755, 0.006242713191445537, 7.27318930755999, 5.558526839089887, 0.712320834776673, 5.950710855665232 };
        
        // Wrong
        long count = Stream.of(array).count();
        Double sum = Stream.of(array).reduce(0.0, (v1, v2) -> v1 + v2);
        System.out.println("sum = " + sum + ", avg = " + (sum/count));
        
        Double sum2 = Stream.of(array).mapToDouble(Double::doubleValue).sum();
        System.out.println("sum2 = " + sum2 + ", avg = " + (sum2/count));
        
        OptionalDouble avg3 = Stream.of(array).mapToDouble(Double::doubleValue).average();
        System.out.println("sum3 = undefined, avg = " + avg3.orElse(Double.NaN));
        
        // Suitable
        Avg avg4 = Stream.of(array).reduce(Avg.empty(), Avg::collect, Avg::combine);
        System.out.println("sum4 = " + avg4.getValue() + ", avg = " + avg4.avg());
    }

    private static class Avg {
        private final Double value;
        private final long times;
        
        public static Avg empty() {
            return new Avg();
        }
        
        private Avg() {
            this(0.0, 0);
        }

        private Avg(Double value, long times) {
            this.value = value;
            this.times = times;
        }

        public Avg collect(double value) {
            return new Avg(this.value + value, this.times + 1);
        }
        
        public Avg combine(Avg avg) {
            return new Avg(this.value + avg.value, this.times + avg.times);
        }
        
        public Double avg() {
            return times == 0 ? Double.NaN : value / times;
        }
        
        public Double getValue() {
            return value;
        }        
    }
    
    static void task12() throws Exception {
        String contents = new String(Files.readAllBytes(Paths.get(WAR_AND_PEACE_TXT)),
                StandardCharsets.UTF_8);

        List<String> wordList = Arrays.asList(contents.split("[\\P{L}]+"));

        AtomicInteger[] counters = IntStream.range(0, LENGTH)
                .mapToObj((i) -> new AtomicInteger())
                .toArray(AtomicInteger[]::new);
        
        wordList.stream().parallel()
        .filter((w) -> w.length() < LENGTH)
        .forEach((w) -> counters[w.length()].incrementAndGet());
        
        System.out.println("Counters:");
        IntStream.range(0, LENGTH).forEach((i) -> System.out.println(i + ": " + counters[i].get()));
        
        AtomicIntegerArray counters2 = new AtomicIntegerArray(LENGTH);
        
        wordList.stream().parallel()
        .filter((w) -> w.length() < LENGTH)
        .forEach((w) -> counters2.incrementAndGet(w.length()));

        System.out.println("Counters2:");
        IntStream.range(0, LENGTH).forEach((i) -> System.out.println(i + ": " + counters2.get(i)));

    }
    
    static void task13() throws Exception {
        String contents = new String(Files.readAllBytes(Paths.get(WAR_AND_PEACE_TXT)),
                StandardCharsets.UTF_8);
        List<String> wordList = Arrays.asList(contents.split("[\\P{L}]+"));
        
        wordList.stream()
        .collect(Collectors.groupingBy(String::length, Collectors.counting()))
        .entrySet().stream()
        .filter((e) -> e.getKey() < LENGTH)
        .forEach(System.out::println);        
    }
}
