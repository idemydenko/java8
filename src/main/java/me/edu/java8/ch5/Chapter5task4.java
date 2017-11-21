package me.edu.java8.ch5;

import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;

public class Chapter5task4 {

	public static void main(String[] args) throws Exception {
		cal("01 2018");
	}

	static void cal(String monthOfYear) {
		Objects.requireNonNull(monthOfYear, "month is null");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
		TemporalAccessor ta = formatter.parse(monthOfYear);
		
		System.out.println(ta);
	}
}
