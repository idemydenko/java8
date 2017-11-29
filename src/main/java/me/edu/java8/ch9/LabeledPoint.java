package me.edu.java8.ch9;

import java.util.Objects;

class LabeledPoint implements Comparable<LabeledPoint> {
	int x;
	int y;
	String label;

	public LabeledPoint(int x, int y, String label) {
		this.x = x;
		this.y = y;
		this.label = label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof LabeledPoint)) {
			return false;
		}

		LabeledPoint cast = (LabeledPoint) obj;

		if (label == null) {
			if (cast.label != null) {
				return false;
			}
		} else if (!label.equals(cast.label)) {
			return false;
		} else if (x != cast.x) {
			return false;
		} else if (y != cast.y) {
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(LabeledPoint o) {
		Objects.requireNonNull(o);
		
		int diff = x > o.x ? 1 : x < o.x ? -1 : 0;
		
		if (diff != 0) {
			return diff;
		}
		
		diff = y > o.y ? 1 : y < o.y ? -1 : 0;
		
		if (diff != 0) {
			return diff;
		}
		
		if (Objects.isNull(label) && Objects.nonNull(o.label )) {
			return -1;
		}

		if (Objects.nonNull(label) && Objects.isNull(o.label )) {
			return 1;
		}

		return label.compareTo(o.label);
	}

}
