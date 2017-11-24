package me.edu.java8.ch7;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class Chapter7 {

	public static void print(ScriptEngineFactory factory) {
		Stream.of(factory.getClass().getMethods())
		.filter((m) -> m.getParameterCount() == 0)
		.filter((m) -> m.getModifiers() == Method.DECLARED)
		.forEach((m) -> {
			try {
				System.out.println(m.getName() + ": " + m.invoke(factory));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	static void info() throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();		
		manager.getEngineFactories().forEach((f) -> print(f));
		
		System.out.println();
		ScriptEngine engine = manager.getEngineByName("nashorn");
		Object script = engine.eval("'Hello!'.slice(-4);");
		System.out.println(script);
	}

	public static void main(String[] args) throws Exception {
		info();
	}
}
