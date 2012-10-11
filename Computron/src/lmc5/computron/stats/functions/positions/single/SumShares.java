package lmc5.computron.stats.functions.positions.single;

import java.math.BigDecimal;

import lmc5.computron.common.beans.Trade;

import com.google.common.base.Function;

public class SumShares implements Function<Iterable<Trade>, BigDecimal> {
	/**
	 * Get the sum of all shares in a collection of trades
	 */
	public BigDecimal apply(Iterable<Trade> trades) {
		BigDecimal total = BigDecimal.ZERO;
		for (Trade trade : trades)
			total = total.add(new BigDecimal(trade.getShares()));

		return total;
	}
}
