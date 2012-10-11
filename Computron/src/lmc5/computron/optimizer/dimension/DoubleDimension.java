package lmc5.computron.optimizer.dimension;

import java.util.Random;

public class DoubleDimension extends Dimension<Double> {

	public DoubleDimension(Double min, Double max) {
		super(min, max);
	}

	@Override
	public Double randomPoint(Random r) {
		Double range = getMax() - getMin();
		Double coord = getMin() + (range * r.nextDouble());
		
		return coord;
	}
}
