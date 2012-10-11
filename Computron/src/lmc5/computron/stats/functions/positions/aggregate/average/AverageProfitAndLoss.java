package lmc5.computron.stats.functions.positions.aggregate.average;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.positions.aggregate.count.CountClosed;
import lmc5.computron.stats.functions.positions.aggregate.total.TotalWinValue;
import lmc5.computron.stats.util.MathUtil;

import com.google.common.base.Function;

public class AverageProfitAndLoss implements Function<Iterable<Position>, BigDecimal> {
	public BigDecimal apply(Iterable<Position> positions) {
		BigDecimal closedCount = new CountClosed().apply(positions);

		if (closedCount.signum() == 0)
			// no closed trades, average PNL is Zero?
			return BigDecimal.ZERO;
		else
			return new TotalWinValue().apply(positions).divide(closedCount, MathUtil.MATH_CONTEXT);
	}
}
