package me.edu.java8.ch1.task11;

public class C2 extends S implements I {

	public static void main(String[] args) {
		C2 c = new C2();
		c.f();
	}
	
	@Override
	public void f() {
		System.out.println("C2");
	}
}
