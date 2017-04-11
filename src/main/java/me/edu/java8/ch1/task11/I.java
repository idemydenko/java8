package me.edu.java8.ch1.task11;

public interface I {
	
//	static void f() { System.out.println("static I"); }
//	void f();
	default void f() {
		System.out.println("Default I");
	}

}
