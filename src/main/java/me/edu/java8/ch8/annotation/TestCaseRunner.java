package me.edu.java8.ch8.annotation;

import java.lang.reflect.Method;
import java.util.stream.IntStream;

public class TestCaseRunner {
	private final Class<?> clazz;
	private final Object instance;
	
	public TestCaseRunner(Class<?> clazz) throws Exception {
		this.clazz = clazz;
		this.instance = clazz.newInstance();
	}
	
	public void run() throws Exception {
		for (Method m : clazz.getDeclaredMethods()) {
			TestCase testCase = m.getAnnotation(TestCase.class);

			if (testCase != null) {
				int expected = testCase.expected();
				Object[] params = IntStream.of(testCase.params()).boxed().toArray(Integer[]::new);
				Object o = m.invoke(instance, params);
				
				System.out.println(m.getName() + " - " + (Integer.valueOf(expected).equals(o)? "Success" : "Failed: " + (o + " != " + expected)));
				System.out.println();
			}
		}
	}
}
