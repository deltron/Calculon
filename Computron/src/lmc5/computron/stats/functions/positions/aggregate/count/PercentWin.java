/*
 * Created on May 15, 2008
 */
package lmc5.computron.stats.functions.positions.aggregate.count;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.util.MathUtil;

import com.google.common.base.Function;

/**
 * Count winning positions.
 * 
 * This excludes positions with 0 profit, they are counted as losing.
 * 
 */
public class PercentWin implements Function<Iterable<Position>, BigDecimal> {

	public BigDecimal apply(Iterable<Position> positions) {
		BigDecimal closed = new CountClosed().apply(positions);
		
		if (closed.signum() == 0)
			// no closed positions, return Zero
			return BigDecimal.ZERO;
		else
			return new CountWin().apply(positions).divide(closed, MathUtil.MATH_CONTEXT);
	}
}
