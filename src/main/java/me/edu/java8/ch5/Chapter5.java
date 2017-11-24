package me.edu.java8.ch5;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
	
	static void task5() throws Exception {
		alive("My", "1984-09-21");
		alive("Other", "1987-05-29");
		alive("Other2", "1957-03-08");
		alive("Other3", "1959-09-23");
	}
	
	static void alive(String msg, String dateOfBirth) {
		LocalDate dob = LocalDate.parse(dateOfBirth, DateTimeFormatter.ISO_LOCAL_DATE);

		Period period = Period.between(dob, LocalDate.now());

		System.out.println(msg + ": " + dob);
		System.out.println("Days:    " + toTotalDays(dob, LocalDate.now()));
		System.out.println("Monthes: " + period.toTotalMonths());
		System.out.println("Years:   " + period.getYears());
		System.out.println();
	}
	
	static long toTotalDays(LocalDate start, LocalDate end) {
		long result = start.query((ta) -> ta.range(ChronoField.DAY_OF_YEAR).getMaximum() - ta.get(ChronoField.DAY_OF_YEAR));
		
		result += IntStream.range(start.getYear() + 1, end.getYear())
				.map((y) -> IsoChronology.INSTANCE.isLeapYear(y) ? 366 : 365)
				.sum();		
		
		result += end.query((ta) -> ta.get(ChronoField.DAY_OF_YEAR));
		
		return result;
	}

	static void task6() {
		LocalDate start = LocalDate.of(1900, 1, 13);
		Stream<LocalDate> stream = streamOf(start, (ld) -> ld.plusMonths(1), (ld) -> ld.isAfter(LocalDate.now()));
		stream.filter((d) -> d.getDayOfWeek().equals(DayOfWeek.FRIDAY)).forEach(System.out::println);
	}
	
	static <T> Stream<T> streamOf(T seed, UnaryOperator<T> s, Predicate<T> p) {
		Iterator<T> it = new Iterator<T>() {
			final UnaryOperator<T> op = s;
			final Predicate<T> last = p;
			T current = seed;
			T next = null;
			
			@Override
			public boolean hasNext() {
				if (Objects.isNull(next)) {
					next = op.apply(current);
				}
				return !last.test(next);
			}
			
			@Override
			public T next() {				
				if (Objects.nonNull(next)) {
					current = next;
					next = null;
				} else {
					current = op.apply(current);
				}
				
				if (last.test(current)) {
					throw new NoSuchElementException();
				}
				return current;
			}
		};
			
		Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(it, Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED);
		return StreamSupport.stream(spliterator, false);
	}
 	
	static void task8() {
		Instant now = Instant.now();		
		ZoneId.getAvailableZoneIds().stream()
			.map(ZoneId::of)
			.map(now::atZone)
			.map((zdt) -> zdt.getZone().getDisplayName(TextStyle.NARROW, Locale.getDefault()) + " " + zdt.getOffset())
			.sorted()
			.forEach(System.out::println);
	}

	static void task9() {
		Instant now = Instant.now();		
		ZoneId.getAvailableZoneIds().stream()
			.map(ZoneId::of)
			.map(now::atZone)
			.filter((zdt) -> zdt.getOffset().getTotalSeconds() % 3600 != 0)
			.map((zdt) -> zdt.getZone().getDisplayName(TextStyle.NARROW, Locale.getDefault()) + " " + zdt.getOffset())
			.sorted()
			.forEach(System.out::println);
	}

	static void task10() {
		ZoneId us_la = ZoneId.of("America/Los_Angeles");
		ZoneId de_fr = ZoneId.of("Europe/Berlin");	
		
		ZonedDateTime takeOff = ZonedDateTime.of(LocalDate.now(), LocalTime.of(15, 05), us_la);
		ZonedDateTime land = takeOff.plusHours(10).plusMinutes(50).toInstant().atZone(de_fr);
		System.out.println(land);
	}

	static void task11() {
		ZoneId us_la = ZoneId.of("America/Los_Angeles");
		ZoneId de_fr = ZoneId.of("Europe/Berlin");	
		
		ZonedDateTime takeOff = ZonedDateTime.of(LocalDate.now(), LocalTime.of(14, 05), de_fr);
		ZonedDateTime land = ZonedDateTime.of(LocalDate.now(), LocalTime.of(16, 10), us_la);
		System.out.println(takeOff);
		System.out.println(land);
		Duration duration = Duration.between(takeOff, land);
		System.out.println(duration);
	}
	
	static void task12() {
		
	}
	
	public static void main(String[] args) {
		task11();
	}

}
