package lmc5.computron.stats.functions.positions.single;

import java.math.BigDecimal;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectEntryTrades;
import lmc5.computron.stats.util.MathUtil;

import org.apache.log4j.Logger;

import com.google.common.base.Function;

/**
 * get the average price paid for a list of trades
 */
public class AverageEntryPrice implements Function<Position, BigDecimal> {
	private static final Logger LOGGER = Logger.getLogger(AverageEntryPrice.class);

	public BigDecimal apply(Position position) {
		Iterable<Trade> entry = new SelectEntryTrades().apply(position);

		BigDecimal shares = new SumShares().apply(entry);
		if (shares.signum() == 0)
			// no shares, average price is Zero
			return BigDecimal.ZERO;
		else 
			return new SumValue().apply(entry).divide(shares, MathUtil.MATH_CONTEXT);
	}
}
