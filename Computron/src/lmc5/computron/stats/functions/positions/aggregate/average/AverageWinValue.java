package lmc5.computron.stats.functions.positions.aggregate.average;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.positions.aggregate.count.CountWin;
import lmc5.computron.stats.functions.positions.aggregate.total.TotalWinValue;
import lmc5.computron.stats.util.MathUtil;

import com.google.common.base.Function;

public class AverageWinValue implements Function<Iterable<Position>, BigDecimal> {
	public BigDecimal apply(Iterable<Position> positions) {
		BigDecimal winningCount = new CountWin().apply(positions);
		
		if (winningCount.signum() == 0)
			// no winning trades, average win is Zero
			return BigDecimal.ZERO;
		else
			return new TotalWinValue().apply(positions).divide(winningCount, MathUtil.MATH_CONTEXT);
	}
}
