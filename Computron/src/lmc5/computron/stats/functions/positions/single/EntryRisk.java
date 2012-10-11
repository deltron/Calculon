package lmc5.computron.stats.functions.positions.single;

import java.math.BigDecimal;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectEntryTrades;

import org.apache.log4j.Logger;

import com.google.common.base.Function;

public class EntryRisk implements Function<Position, BigDecimal> {
	private static final Logger LOGGER = Logger.getLogger(EntryRisk.class);

	public BigDecimal apply(Position position) {
		BigDecimal averagePrice = new AverageEntryPrice().apply(position);
		BigDecimal averageStop = new AverageEntryStopPrice().apply(position);
		BigDecimal initialRisk = averagePrice.subtract(averageStop).abs();

		Iterable<Trade> entry = new SelectEntryTrades().apply(position);
		return initialRisk.multiply(new SumShares().apply(entry));
	}
}
