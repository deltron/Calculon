package lmc5.computron.stats.functions.positions.aggregate.total;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectWinningPositions;
import lmc5.computron.stats.functions.operations.Sum;
import lmc5.computron.stats.functions.positions.single.ProfitAndLoss;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class TotalWinValue implements Function<Iterable<Position>, BigDecimal> {
	public BigDecimal apply(Iterable<Position> positions) {
		Iterable<Position> winningPositions = new SelectWinningPositions().apply(positions);
		return new Sum().apply(Iterables.transform(winningPositions, new ProfitAndLoss()));
	}
}
