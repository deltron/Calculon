package lmc5.computron.stats.functions.positions.single;

import java.math.BigDecimal;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectEntryTrades;
import lmc5.computron.stats.functions.filters.SelectExitTrades;

import com.google.common.base.Function;

/**
 * Calculate the profit of loss on a single position
 * 
 * todo: This calculation seems to be valid to the long side only. The result
 * would give a negative pnl for a short sell
 */
public class ProfitAndLoss implements Function<Position, BigDecimal> {
	public BigDecimal apply(Position position) {
		Iterable<Trade> entryTrades = new SelectEntryTrades().apply(position);
		Iterable<Trade> exitTrades = new SelectExitTrades().apply(position);
		
		return new SumValue().apply(exitTrades).subtract(new SumValue().apply(entryTrades));
	}
}
