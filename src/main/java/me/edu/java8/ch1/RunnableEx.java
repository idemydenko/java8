package me.edu.java8.ch1;

public interface RunnableEx {
	void run() throws Exception;
	
	public static Runnable unchecked(RunnableEx runner) {
		return () -> {
			try {
				runner.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
}
