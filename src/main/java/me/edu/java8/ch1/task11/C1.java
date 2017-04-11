package me.edu.java8.ch1.task11;

public class C1 implements I, J {

	@Override
	public void f() {
		System.out.println("C1");
	}

	public static void main(String[] args) {
		C1 c = new C1();
		c.f();
	}
}
