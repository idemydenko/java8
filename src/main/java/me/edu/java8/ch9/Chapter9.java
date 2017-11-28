package me.edu.java8.ch9;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Chapter9 {

	static void task1() {

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
	}

	static void task2() {
		Scanner in = null;
		PrintWriter out = null;
		Exception throwable = null;
		
		try {
			in = new Scanner(System.in);
			out = new PrintWriter("/path/out.txt");
			
			while (in.hasNext()) {
				out.println(in.next());
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throwable = ioe;
		} finally {
			if (Objects.nonNull(out)) {
				try {
					out.close();
				} catch (Exception e) {
					if (Objects.nonNull(throwable)) {
						throwable.addSuppressed(e);
					}
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					if (Objects.nonNull(throwable)) {
						throwable.addSuppressed(e);
					}
				}
			}			
		}		
	}

	
	static void task3() throws IOException, NoSuchMethodException {
		try {
			Scanner scanner = new Scanner(new File("/file"));
			InetAddress address = InetAddress.getLocalHost();
			
			scanner.getClass().getMethod("method1");
			scanner.close();
			address.isAnyLocalAddress();
		} catch (FileNotFoundException | UnknownHostException | NoSuchMethodException e) {
			throw e;
		}
	}

	static void task5() throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get("./res", "alphabet.txt"));
		byte[] out = new byte[bytes.length];
		
		for (int i = 0; i < bytes.length; i++) {
			out[out.length - i - 1] = bytes[i];
		}
		Files.write(Paths.get("./res", "revers_alphabet.txt"), out, StandardOpenOption.CREATE);
				
	}
	
	static void task6() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./res", "cities.txt"));
		Collections.reverse(lines);
		Files.write(Paths.get("./res", "revers_cities.txt"), lines, StandardOpenOption.CREATE);
	}
	
	static void task7() throws IOException {
		try (InputStream is = new URL("http://reuters.com").openStream()) {
			Files.copy(is, Paths.get("res", "reuters.html"), StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	public static void main(String[] args) throws Exception {
		task7();
	}

}
