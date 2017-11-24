package me.edu.java8.ch5;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Chapter3task7 {

	public static class TimeInterval {
			
		private final LocalDate date;
		private final LocalTime from;
		private final LocalTime to;
		
		public static TimeInterval on(LocalDate date, LocalTime from, LocalTime to) {
			Objects.requireNonNull(date);
			Objects.requireNonNull(from);
			Objects.requireNonNull(to);
			
			if (from.isAfter(to)) {
				throw new IllegalArgumentException();
			}
			
			return new TimeInterval(date, from, to);
		}
		
		private TimeInterval(LocalDate date, LocalTime from, LocalTime to) {
			
			this.date = date;
			this.from = from;
			this.to = to;
		}
		
		public boolean isOverlapped(TimeInterval other) {
			if (!date.equals(other.date)) {
				return false;
			}
		
			if (to.isBefore(other.from)) {
				return false;
			}
			
			if (from.isAfter(other.to)) {
				return false;
			}
			return true;
		}
		
		@Override
		public String toString() {
			return date + " @ [" + from + ", " + to + "]";
		}
	}
	
	public static void main(String[] args) {
		TimeInterval ti1 = TimeInterval.on(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 00));		
		TimeInterval ti2 = TimeInterval.on(LocalDate.now(), LocalTime.of(10, 30), LocalTime.of(10, 35));
		TimeInterval ti3 = TimeInterval.on(LocalDate.now(), LocalTime.of(9, 30), LocalTime.of(10, 15));
		TimeInterval ti4 = TimeInterval.on(LocalDate.now(), LocalTime.of(10, 50), LocalTime.of(11, 15));
		TimeInterval ti5 = TimeInterval.on(LocalDate.now(), LocalTime.of(11, 30), LocalTime.of(12, 15));
		
		System.out.println(ti1.isOverlapped(ti2) + " = " + ti2.isOverlapped(ti1));
		System.out.println(ti1.isOverlapped(ti3) + " = " + ti3.isOverlapped(ti1));
		System.out.println(ti1.isOverlapped(ti4) + " = " + ti4.isOverlapped(ti1));
		System.out.println(ti1.isOverlapped(ti5) + " = " + ti5.isOverlapped(ti1));

	}

}
