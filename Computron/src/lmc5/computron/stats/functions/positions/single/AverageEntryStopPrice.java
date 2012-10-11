package lmc5.computron.stats.functions.positions.single;

import java.math.BigDecimal;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectEntryTrades;
import lmc5.computron.stats.util.MathUtil;

import com.google.common.base.Function;

/**
 * Average Stop Loss price for a collection of trades
 */
public class AverageEntryStopPrice implements Function<Position, BigDecimal> {

	public BigDecimal apply(Position position) {
		BigDecimal totalStop = BigDecimal.ZERO;
		for (Trade trade : new SelectEntryTrades().apply(position)) {
			BigDecimal shares = new BigDecimal(trade.getShares());
			BigDecimal grossStop = trade.getStop_price().multiply(shares);
			totalStop = totalStop.add(grossStop);
		}

		BigDecimal shares = new SumShares().apply(new SelectEntryTrades().apply(position));
		if (shares.signum() == 0)
			// no shares, average stop is Zero
			return BigDecimal.ZERO;
		else 
			return totalStop.divide(shares, MathUtil.MATH_CONTEXT);
	}
}
