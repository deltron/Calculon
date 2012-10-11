package lmc5.computron.stats.functions.positions.single;

import java.math.BigDecimal;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectEntryTrades;
import lmc5.computron.stats.functions.filters.SelectExitTrades;

import com.google.common.base.Function;

/**
 * sum of all buy minus all sells
 * 
 */
public class NetShareAmount implements Function<Position, BigDecimal> {
	public BigDecimal apply(Position position) {
		Iterable<Trade> entry = new SelectEntryTrades().apply(position);
		Iterable<Trade> exit = new SelectExitTrades().apply(position);

		return new SumShares().apply(entry).subtract(new SumShares().apply(exit));
	}
}
