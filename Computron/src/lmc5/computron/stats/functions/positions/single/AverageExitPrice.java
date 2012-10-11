package lmc5.computron.stats.functions.positions.single;

import java.math.BigDecimal;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectExitTrades;
import lmc5.computron.stats.util.MathUtil;

import org.apache.log4j.Logger;

import com.google.common.base.Function;

/**
 * get the average exit price for a position
 */
public class AverageExitPrice implements Function<Position, BigDecimal> {
	private static final Logger LOGGER = Logger.getLogger(AverageExitPrice.class);

	public BigDecimal apply(Position position) {
		Iterable<Trade> exit = new SelectExitTrades().apply(position);
		
		BigDecimal shares = new SumShares().apply(exit);
		if (shares.signum() == 0)
			// no shares, average price is Zero
			return BigDecimal.ZERO;
		else 
			return new SumValue().apply(exit).divide(shares, MathUtil.MATH_CONTEXT);
	}
}
