package lmc5.computron.optimizer.dimension;

import java.util.Random;

public abstract class Dimension<N extends Number> {
	private N min;
	private N max;
	private String name;

	public Dimension(String name, N min, N max) {
		this.name = name;
		this.min = min;
		this.max = max;
	}

	public Dimension(N min, N max) {
		this(null, min, max);
	}

	public N getMax() {
		return max;
	}

	public N getMin() {
		return min;
	}

	public String getName() {
		return name;
	}

	public abstract N randomPoint(Random r);

	@Override
	public String toString() {
		String s = "[";
		if (name != null)
			s += name + ",";
		s += min + "," + max + "]";
		return s;
	}

}
