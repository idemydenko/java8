package me.edu.java8.ch3;


import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;



public final class Chapter3 {

    public static void main(String[] args) throws Exception {
        task7();
    }

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
}
