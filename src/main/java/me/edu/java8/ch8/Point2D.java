package me.edu.java8.ch8;

import java.util.Comparator;

public class Point2D {
	
	public static final Comparator<Point2D> CMP = new Comparator<Point2D>() {
		@Override
		public int compare(Point2D o1, Point2D o2) {
			
			return Comparator
					.comparingInt(Point2D::getX)
					.thenComparingInt(Point2D::getY)
					.compare(o1, o2);
		}
	};
	
	private final int x;
	private final int y;
	
	public Point2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Point2D)) {
			return false;
		}

		Point2D other = (Point2D) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}
	
	
}
