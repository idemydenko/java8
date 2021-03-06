package me.edu.java8.ch3;


import java.lang.reflect.Field;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;




public final class Chapter3 {

    static void task1() throws Exception {
        int[] a = IntStream.iterate(100, (i) -> i + 1).limit(20).toArray();

        IntStream.range(0, a.length).forEach((i) -> {
            logIf(Logger.getGlobal(), Level.INFO, () -> i == 10,
                    () -> "a[10] = " + a[10]);
        });
    }

    static <T> void logIf(final Logger log, final Level level,
            Supplier<Boolean> condition, Supplier<String> message) {
        if (log.isLoggable(level) && condition.get()) {
            log.log(level, message.get());
        }
    }

    static void task2() throws Exception {
        ReentrantLock lock = new ReentrantLock();
        withLock(lock, () -> System.out.println("Action!"));
    }

    static void withLock(ReentrantLock lock, Runnable action) {
        lock.lock();
        try {
            action.run();
        } finally {
            lock.unlock();
        }
    }

    static void task7() throws Exception {
        Collection<String> strings = Arrays.asList("One", "t wo", "ThreE", "Fo ur", "five");
        
        System.out.println(">>>>>>> false, false, false");
        strings.stream().sorted(comparator(false, false, false)).forEach(System.out::println);
        System.out.println(">>>>>>> true, false, false");
        strings.stream().sorted(comparator(true, false, false)).forEach(System.out::println);
        System.out.println(">>>>>>> false, true, false");
        strings.stream().sorted(comparator(false, true, false)).forEach(System.out::println);
        System.out.println(">>>>>>> false, false, true");
        strings.stream().sorted(comparator(false, false, true)).forEach(System.out::println);
        System.out.println(">>>>>>> false, true, true");
        strings.stream().sorted(comparator(false, true, true)).forEach(System.out::println);

    }
    
    static Comparator<String> comparator(boolean revers, boolean ignoreCase, boolean ignoreWhitespace) {
        return (s1, s2) -> {            
            if (s1 == null) {
                return -1;
            }

            if (s2 == null) {
                return 1;
            }

            IntStream stream1 = s1.chars()
                    .map((ch) -> ignoreCase ? Character.toLowerCase(ch) : ch)
                    .filter((ch) -> !ignoreWhitespace || !Character.isWhitespace(ch));
            IntStream stream2 = s2.chars()
                    .map((ch) -> ignoreCase ? Character.toLowerCase(ch) : ch)
                    .filter((ch) -> !ignoreWhitespace || !Character.isWhitespace(ch));
            
            StringBuilder rs1 = stream1.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
            StringBuilder rs2 = stream2.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
            
            if (revers) {
                rs1.reverse();
                rs2.reverse();
            }
            
            return rs1.toString().compareTo(rs2.toString());
        };
    }
    
    static void task9() throws Exception {
        int result = lexicographicComparator("f1", "f2").compare("sq", "sq");
        System.out.println(result);
    }
    
    static <T> Comparator<T> lexicographicComparator(String... fieldNames) {
        return (o1, o2) -> {
            if (o1 == null) {
                return -1;
            }
            
            if (o2 == null) {
                return 1;
            }
            
            for (String name : fieldNames) {
                Field f1 = null;
                try {
                    f1 = o1.getClass().getField(name); 
                } catch (NoSuchFieldException e) {}
                
                Field f2 = null; 
                try {
                    f2 = o2.getClass().getField(name); 
                } catch (NoSuchFieldException e) {}
                
                if (f1 == null && f2 == null) {
                    continue;
                }
                
                if (f1 == null) {
                    return -1;
                }
                
                if (f2 == null) {
                    return 1;
                }
                
                f1.setAccessible(true);
                f2.setAccessible(true);
                
                Object v1 = null;
                try {
                    v1 = f1.get(o1);
                } catch (IllegalAccessException e) {}
                
                Object v2 = null;
                try {
                    v2 = f2.get(o2);
                } catch (IllegalAccessException e) {}

                if (v1 == null && v2 == null) {
                    continue;
                }
                
                if (v1 == null) {
                    return -1;
                }
                
                if (v2 == null) {
                    return 1;
                }
              
                if (!v1.equals(v2)) {
                    return v1.toString().compareTo(v2.toString());
                }
            }
            
            return 0;
        };
    }
    
    static void task10() throws Exception {
//    	final Image im = null;
    	
//        UnaryOperator<Color> op = Color::brighter;
//        Chapter3task5.transform(im, op.compose(Color::grayscale));
        
    }

    
    static <T, U> U reduce(Stream<T> stream, U indentity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
    	return stream.reduce(indentity, accumulator, combiner);
    }
    
    static void task19() {
    	List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
    	Integer sum = reduce(list.stream(), -21, (s, e) -> s + e, (s1, s2) -> s1 + s2 );
    	System.out.println("sum: " + sum);
    	
    	System.out.println();
    	String row = reduce(list.stream(), "Row\n", (s, e) -> s + " " + e + "\n", (s1, s2) -> s1 + s2);
    	System.out.println(row);

    }
    
    static <T, U> List<U> map(List<T> list, Function<T, U> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    static void task20() throws Exception {
        List<Integer> l = IntStream.range(0, 20).mapToObj(Integer::valueOf).collect(Collectors.toList());
        List<String> r = map(l, Integer::toBinaryString);
        System.out.println(r);
    }
    
    static <T, U> Future<U> map(Future<T> f, Function<T, U> mapper) {
        return new Future<U>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return f.cancel(mayInterruptIfRunning);
            }
            
            @Override
            public boolean isDone() {
                return f.isDone();
            }
            
            @Override
            public boolean isCancelled() {
                return f.isCancelled();
            }
            
            @Override
            public U get() throws InterruptedException, ExecutionException {
                return mapper.apply(f.get());
            }
            
            @Override
            public U get(long timeout, TimeUnit unit)
                    throws InterruptedException, ExecutionException,
                    TimeoutException {
                return mapper.apply(f.get(timeout, unit));
            }
        };
    }
    
    static void task21() throws Exception {
        ExecutorService service = Executors.newCachedThreadPool();
        Future<Instant> f = service.submit(() -> {
            TimeUnit.SECONDS.sleep(5);
            return Instant.now();
        });
        
        Future<String> f2 = map(f, (i) -> i.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME));
        System.out.println(f2.get());
        
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
    }
    
    static void task22() throws Exception {
    	CompletableFuture<Void> cf = CompletableFuture
    			.supplyAsync(() -> Stream.of(Month.values()))
    			.thenApply((m) -> m.map(Object::toString))
    			.thenCompose((m) -> CompletableFuture.runAsync(() -> m.forEach(System.out::println)));
    	cf.get();
    }
    
    public static void main(String[] args) throws Exception {
        task22();
    }

}
