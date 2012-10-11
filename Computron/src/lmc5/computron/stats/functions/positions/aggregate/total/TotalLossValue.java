package lmc5.computron.stats.functions.positions.aggregate.total;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectLosingPositions;
import lmc5.computron.stats.functions.operations.Sum;
import lmc5.computron.stats.functions.positions.single.ProfitAndLoss;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class TotalLossValue implements Function<Iterable<Position>, BigDecimal> {
	public BigDecimal apply(Iterable<Position> positions) {
		Iterable<Position> losingPositions = new SelectLosingPositions().apply(positions);
		return new Sum().apply(Iterables.transform(losingPositions, new ProfitAndLoss())).abs();
	}
}
