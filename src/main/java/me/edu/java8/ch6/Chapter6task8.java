package me.edu.java8.ch6;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class Chapter6task8 {

	public static void main(String[] args) {		
		final int epsilon = 2;
		final int initSize = 10000;

		int size = initSize;
		int shift = size;
		
		while (shift > epsilon) {
			int seqMorePar = sort(size);
			
			if (seqMorePar == 0) {
				seqMorePar++;
			}
			int sign = Integer.signum(seqMorePar);
			shift = shift / 2;
			
			System.out.println(size + "\n----\n");
			size += shift * sign;
		}
		System.out.println(size);
	}
	
	static int sort(int size) {
		int[] array = array(size);

		Instant start = Instant.now();
		Arrays.sort(Arrays.copyOf(array, array.length));
		Duration seq = Duration.between(start, Instant.now());
		
		start = Instant.now();
		Arrays.sort(Arrays.copyOf(array, array.length));
		Duration par = Duration.between(start, Instant.now());
		
		System.out.println("Equal time: " + seq.equals(par));
		System.out.println("Seq: " + seq + " (" + seq.toMillis() + " mills)");
		System.out.println("Par: " + par + " (" + par.toMillis() + " mills)");
		
		
		return par.compareTo(seq);
	}
	
	static int[] array(int size) {
		return new Random().ints(size).toArray();		
	}
}
