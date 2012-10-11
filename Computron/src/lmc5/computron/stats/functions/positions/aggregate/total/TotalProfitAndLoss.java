package lmc5.computron.stats.functions.positions.aggregate.total;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;

import com.google.common.base.Function;

/**
 * Calculate the profit of loss on a collection of trades.
 * 
 * todo: This calculation seems to be valid to the long side only. The result
 * would give a negative pnl for a short sell
 */
public class TotalProfitAndLoss implements Function<Iterable<Position>, BigDecimal> {
	public BigDecimal apply(Iterable<Position> positions) {
		return new TotalWinValue().apply(positions).subtract(new TotalLossValue().apply(positions));
	}
}
