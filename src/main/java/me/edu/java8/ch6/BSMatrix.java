package me.edu.java8.ch6;

public class BSMatrix {

	final int tl;
	final int tr;
	final int bl;
	final int br;
	
	public BSMatrix(int tl, int tr, int bl, int br) {
		this.tl = tl;
		this.tr = tr;
		this.bl = bl;
		this.br = br;
	}
	
	public BSMatrix mult(BSMatrix other) {
		int tl = this.tl * other.tl + this.tr * other.bl;
		int tr = this.tl * other.tr + this.tr * other.br;
		int bl = this.bl * other.tl + this.br * other.bl;
		int br = this.bl * other.tr + this.br * other.br;
		
		return new BSMatrix(tl, tr, bl, br);
	}
	
	public static BSMatrix one() {
		return new BSMatrix(1, 1, 1, 0);
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("\n[[").append(tl).append(", ").append(tr).append("], [").append(bl).append(", ").append(br).append("]]").toString();
	}
}
