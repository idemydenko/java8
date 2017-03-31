package me.edu.java8.ch1;

public interface RunnableMerge {

	public static Runnable andThan(Runnable first, Runnable second) {
		return () -> {
			first.run();
			second.run();
		};
	}
}
