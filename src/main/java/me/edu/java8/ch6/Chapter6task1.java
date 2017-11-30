package me.edu.java8.ch6;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

public class Chapter6task1 {

	private final AtomicReference<String> theLongest = new AtomicReference<String>("");
	private final LongAdder wordsCount = new LongAdder();
	private final LongAccumulator length = new LongAccumulator(Math::max, 0);
	
	public static void main(String[] args) throws Exception {
		String content = new String(Files.readAllBytes(Paths.get("res", "war-and-peace.txt")), StandardCharsets.UTF_8);
		
		final Chapter6task1 inst = new Chapter6task1();
		
		Stream.of(content.split("[\\P{L}]+")).forEach(inst::eval);
		
		System.out.println(inst.wordsCount);
		System.out.println(inst.theLongest);
		System.out.println(inst.theLongest.get().length());
		System.out.println(inst.length);
	}

	void eval(String word) {
		wordsCount.increment();		
		theLongest.accumulateAndGet(word, (String c, String g) -> {
			if (g.length() > c.length()) {
				length.accumulate(g.length());
				return g;
			}
			return c;
		});
	}
}
