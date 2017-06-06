package ch2.sec01;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Test {
    private static final String ALICE_TXT = "../alice.txt";
    private static final String WAR_AND_PEACE_TXT = "../war-and-peace.txt";
    private static final int LENGTH = 12;

    public static void main(String[] args) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(ALICE_TXT)),
                StandardCharsets.UTF_8);
        List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));

        long count = 0;
        for (String w : words) {
            if (w.length() > LENGTH)
                count++;
        }
        System.out.println(count);

        count = words.stream().filter(w -> w.length() > LENGTH).count();
        System.out.println(count);

        count = words.parallelStream().filter(w -> w.length() > LENGTH).count();
        System.out.println(count);
    }
}
