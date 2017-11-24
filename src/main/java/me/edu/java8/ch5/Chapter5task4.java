package me.edu.java8.ch5;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Chapter5task4 {

	private static final List<DateTimeFormatter> MONTH_OF_YEAR_FORMATTERS = Arrays.asList(
				DateTimeFormatter.ofPattern("MM yyyy"),
				DateTimeFormatter.ofPattern("MMM yyyy"),
				DateTimeFormatter.ofPattern("MMMM yyyy")
			); 
	
	private static final DateTimeFormatter DAY_OF_WEEK_FORMATTER = DateTimeFormatter.ofPattern("EE ");
	
	
	public static void main(String[] args) throws Exception {
		cal("01 2018");
		System.out.println();
		
		cal("April 1927");
		System.out.println();
		
		cal("May 1987");
		System.out.println();
		
		cal("Nov 2017");
		System.out.println();

	}

	static YearMonth parse(String monthOfYaer) {
		
		for (DateTimeFormatter formatter : MONTH_OF_YEAR_FORMATTERS) {
			try {
				return YearMonth.parse(monthOfYaer, formatter);
			} catch(DateTimeParseException e) {}
		}
		throw new DateTimeParseException("Couldn't be parsed", monthOfYaer, 0);
	}

	static void print(YearMonth ym) {
		
		final StringBuilder sb = new StringBuilder(MONTH_OF_YEAR_FORMATTERS.get(2).format(ym));
		
		final String nl = System.lineSeparator();
		
		sb.append(nl);
		Stream.of(DayOfWeek.values()).map(DAY_OF_WEEK_FORMATTER::format).forEachOrdered(sb::append);
		sb.append(nl);
		
		LocalDate start = ym.atDay(1);
		
		Stream.iterate(start, (d) -> d.plusDays(1)).limit(ym.lengthOfMonth()).map(Chapter5task4::formatDayOfMonth).forEach(sb::append);
		
		System.out.println(sb);
	}
	
	static String formatDayOfMonth(LocalDate date) {
		int dayOfMonth = date.getDayOfMonth();
		int dow = date.getDayOfWeek().getValue();
		String prefix = " ";
		String suffix = " ";
		
		if (dayOfMonth == 1) {
			prefix += String.join("", Collections.nCopies(dow - 1, "    "));
		}
		
		if (dayOfMonth < 10) {
			prefix += " ";
		}
		
		if (dow % 7 == 0) {
			suffix += System.lineSeparator();
		}

		return String.join("", prefix, String.valueOf(dayOfMonth), suffix);
	}
	
	static void cal(String monthOfYear) {
		Objects.requireNonNull(monthOfYear, "month is null");
		
		YearMonth ym = parse(monthOfYear);	

		print(ym);
	}
	
}
