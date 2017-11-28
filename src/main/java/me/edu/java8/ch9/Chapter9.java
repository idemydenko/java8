package me.edu.java8.ch9;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Scanner;

public class Chapter9 {

	static void task1() throws IOException {

		/*
		 * without try-with-resources  
		 */
		Scanner in = null;
		PrintWriter out = null;
		
		try {
			in = new Scanner(System.in);
			out = new PrintWriter("/path/out.txt");
			
			while (in.hasNext()) {
				out.println(in.next());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		}
		
		/*
		 * with try-with-resources
		 */
		try (
				Scanner scanner = new Scanner(System.in);
				PrintWriter writer = new PrintWriter("/path/out.txt")
				) {
			
			while (in.hasNext()) {
				out.println(in.next());
			}			
		}
	}
	
	public static void main(String[] args) throws Exception {
		
	}

}
