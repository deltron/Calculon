/*
 * Created on May 15, 2008
 */
package lmc5.computron.stats.functions.positions.aggregate.average;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.positions.aggregate.count.CountClosed;
import lmc5.computron.stats.functions.positions.aggregate.total.TotalR;
import lmc5.computron.stats.util.MathUtil;

import com.google.common.base.Function;

/**
 * Average of R for all positions
 */
public class Expectancy implements Function<Iterable<Position>, BigDecimal> {

	public BigDecimal apply(Iterable<Position> positions) {
		BigDecimal totalR = new TotalR().apply(positions);
		BigDecimal closedPositionCount = new CountClosed().apply(positions);
		return totalR.divide(closedPositionCount, MathUtil.MATH_CONTEXT);
	}

}
