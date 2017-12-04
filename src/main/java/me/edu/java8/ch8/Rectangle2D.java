package me.edu.java8.ch8;

import java.util.Comparator;

public class Rectangle2D {
	
	public static final Comparator<Rectangle2D> CMP = new Comparator<Rectangle2D>() {
		@Override
		public int compare(Rectangle2D o1, Rectangle2D o2) {
			return Comparator
					.comparing(Rectangle2D::getPoint, Comparator.nullsLast(Point2D.CMP))
					.thenComparingInt(Rectangle2D::getWidth)
					.thenComparingInt(Rectangle2D::getHeight)
					.compare(o1, o2);
		}
	};
	
	private final Point2D point;
	private final int width;
	private int height;
	
	public Rectangle2D(Point2D point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
	}
	
	public Point2D getPoint() {
		return point;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + height;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Rectangle2D)) {
			return false;
		}

		Rectangle2D other = (Rectangle2D) obj;
		if (height != other.height) {
			return false;
		}
		
		if (width != other.width) {
			return false;
		}
		
		if (point == null) {
			if (other.point != null) {
				return false;
			}
		} else if (!point.equals(other.point)) {
			return false;
		}

		return true;
	}
	
	
}
