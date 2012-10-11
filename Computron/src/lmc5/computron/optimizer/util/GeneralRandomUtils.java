package lmc5.computron.optimizer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lmc5.computron.optimizer.dimension.Dimension;

public class GeneralRandomUtils {
	public static List<Number> randomPointInRange(Random r, List<Dimension> dimensions) {
		List<Number> coord = new ArrayList<Number>();

		for (Dimension dimension : dimensions) {
			coord.add(dimension.randomPoint(r));
		}

		return coord;
	}
}
