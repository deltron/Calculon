package lmc5.computron.stats.functions.positions.single;

import java.math.BigDecimal;

import lmc5.computron.common.beans.Trade;

import com.google.common.base.Function;

/**
 * Sum the values of all trades
 */
public class SumValue implements Function<Iterable<Trade>, BigDecimal> {

	public BigDecimal apply(Iterable<Trade> trades) {
		BigDecimal total = BigDecimal.ZERO;

		for (Trade trade : trades) {
			BigDecimal value = trade.getPrice().multiply(new BigDecimal(trade.getShares()));
			total = total.add(value);
		}

		return total;
	}
}
