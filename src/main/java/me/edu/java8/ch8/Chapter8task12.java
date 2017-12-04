package me.edu.java8.ch8;

import me.edu.java8.ch8.annotation.TestCase;
import me.edu.java8.ch8.annotation.TestCaseRunner;

public class Chapter8task12 {

	public static void main(String[] args) throws Exception {
		TestCaseRunner runner = new TestCaseRunner(Chapter8task12.class);
		runner.run();
	}
	
	@TestCase(expected = 0)
	public int i1() {
		return 0;
	}
	
	@TestCase(expected = 0)
	public int i2() {
		return Integer.MAX_VALUE;
	}

	@TestCase(expected = 1, params = 1)
	public int i3(int i) {
		return i;
	}

	
	@TestCase(expected = 0)
	public String s2() {
		return "String";
	}
}
