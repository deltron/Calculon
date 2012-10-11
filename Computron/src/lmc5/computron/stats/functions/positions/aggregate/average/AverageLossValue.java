package lmc5.computron.stats.functions.positions.aggregate.average;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.positions.aggregate.count.CountLose;
import lmc5.computron.stats.functions.positions.aggregate.total.TotalLossValue;
import lmc5.computron.stats.util.MathUtil;

import com.google.common.base.Function;

public class AverageLossValue implements Function<Iterable<Position>, BigDecimal> {
	public BigDecimal apply(Iterable<Position> positions) {
		BigDecimal losingCount = new CountLose().apply(positions);

		if (losingCount.signum() == 0)
			// no losing trades, average loss is Zero
			return BigDecimal.ZERO;
		else
			return new TotalLossValue().apply(positions).divide(losingCount, MathUtil.MATH_CONTEXT);
	}
}
