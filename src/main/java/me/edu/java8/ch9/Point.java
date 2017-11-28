package me.edu.java8.ch9;


class Point implements Comparable<Point>{
	   private int x;
	   private int y;
	   
	   public int compareTo(Point other) {
	      int diff = x < other.x ? -1 : x == other.x ? 0 : 1;
	      if (diff != 0) return diff;
	      return y < other.y ? -1 : y == other.y ? 0 : 1;
	   }

}
