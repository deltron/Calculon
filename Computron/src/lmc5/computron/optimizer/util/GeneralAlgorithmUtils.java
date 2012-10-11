package lmc5.computron.optimizer.util;

import java.util.List;

import lmc5.computron.optimizer.dimension.Dimension;

/**
 * Rewriting some algorithm utils to use Objects
 * 
 * 
 * 
 */
public class GeneralAlgorithmUtils {

	public static void fixCoordBounds(List<Number> coord, List<Dimension> dimensions) {
		reflectCoord(coord, dimensions);
	}


	public static final boolean inBounds(Number d, Number min, Number max) {
		return d.doubleValue() >= min.doubleValue() && d.doubleValue() <= max.doubleValue() && !isInvalidNumber(d);
	}


	public static final boolean isInvalidNumber(Number d) {
		return Double.isNaN(d.doubleValue()) || Double.isInfinite(d.doubleValue());
	}


	/**
	 * Treats the boundaries of the domain as solid, and reflects coordinates
	 * back into the domain if they lie outside of the domain
	 * 
	 * @param coordinates -
	 *            the coordinate to reflect (if required)
	 * @param minmax -
	 *            minmax - bounds of the coordinate space [] = each dimension
	 *            [i][2] = {min,max} format
	 * 
	 */
	private static void reflectCoord(List<Number> coordinates, List<Dimension> dimensions) {
		int i = 0;

		for (Number coord : coordinates) {
			Double n = coord.doubleValue();
			Double max = dimensions.get(i).getMax().doubleValue();
			Double min = dimensions.get(i).getMin().doubleValue();

			while (n > max || n < min) {
				while (n > max) {
					// subtract the difference
					Double diff = Math.abs(n - max);
					// always smaller
					n = (max - diff);

					coordinates.set(i, n);
				}
				// too small
				while (n < min) {
					Double diff = Math.abs(n - min);
					// always larger
					n = (min + diff);

					coordinates.set(i, n);
				}
			}

			i++;
		}
	}
}
