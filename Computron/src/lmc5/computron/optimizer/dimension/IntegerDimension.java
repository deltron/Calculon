package lmc5.computron.optimizer.dimension;

import java.util.Random;

public class IntegerDimension  extends Dimension<Integer> {
	public IntegerDimension(Integer min, Integer max) {
		super(min, max);
	}
	
	public IntegerDimension(String s, Integer min, Integer max) {
		super(s, min, max);
	}

	@Override
	public Integer randomPoint(Random r) {
		Integer range = getMax() - getMin();
		Integer coord = getMin() + r.nextInt(range);
		
		return coord;
	}
}
