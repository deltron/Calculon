/*
 * Created on May 15, 2008
 */
package lmc5.computron.stats.functions.positions.aggregate.total;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectClosedPositions;
import lmc5.computron.stats.functions.positions.single.RewardToRisk;
import lmc5.computron.stats.util.MathUtil;

import com.google.common.base.Function;

/**
 * Calculate the total R
 */
public class TotalR  implements Function<Iterable<Position>, BigDecimal> {
	
	public BigDecimal apply(Iterable<Position> positions) {
		BigDecimal totalR = MathUtil.ZERO;
		for (Position p: new SelectClosedPositions().apply(positions)) {
			totalR = totalR.add(new RewardToRisk().apply(p));
		}
		return totalR;
	}

}
