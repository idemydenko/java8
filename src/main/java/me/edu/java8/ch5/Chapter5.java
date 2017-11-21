package me.edu.java8.ch5;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.function.Predicate;

public class Chapter5 {

	static void task1() throws Exception {
		final int programmersDay = 256;
		
		Period period = Period.ofDays(programmersDay - 1);
		LocalDate date = LocalDate.of(2018, 01, 01).plus(period);		
		System.out.println(date);
		
		date = LocalDate.of(2018, 01, 01).withDayOfYear(256);
		System.out.println(date);
		
		date = LocalDate.of(2018, 01, 01).with(ChronoField.DAY_OF_YEAR, programmersDay);
		System.out.println(date);
		
		date = LocalDate.of(2018, 01, 01).with((w) -> w.plus(Period.ofDays(programmersDay - 1)));
		System.out.println(date);
		
	}
	
	static void task2() throws Exception {
		LocalDate date = LocalDate.of(2000, 2, 29);
		
		System.out.println(date.plusYears(1));
		System.out.println(date.plusYears(4));
		System.out.println(date.plusYears(1).plusYears(1).plusYears(1).plusYears(1));
	}
	
	static TemporalAdjuster next(Predicate<Temporal> condition) {
		return (d) -> condition.test(d) ? d.plus(7, ChronoUnit.DAYS) : d; 
	}

	static TemporalAdjuster next2(Predicate<LocalDate> condition) {
		return (d) -> {
			if ((d instanceof LocalDate) && (condition.test((LocalDate) d))) { 
				return d.plus(7, ChronoUnit.DAYS); 
			}
				return d;
		};
	}

	
	static void task3() throws Exception {
		LocalDate date = LocalDate.of(2000, 1, 1).with(next((d) -> d.until(LocalDate.now(), ChronoUnit.DAYS) > 0));
		System.out.println(date);
		
		date = LocalDate.of(2018, 1, 1).with(next2((d) -> d.until(LocalDate.now(), ChronoUnit.DAYS) > 0));
		System.out.println(date);

	}
	
	public static void main(String[] args) throws Exception {
		task3();
	}

}
