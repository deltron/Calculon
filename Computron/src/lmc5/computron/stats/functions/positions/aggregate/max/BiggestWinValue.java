package lmc5.computron.stats.functions.positions.aggregate.max;

import java.math.BigDecimal;
import java.util.Collections;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.positions.single.ProfitAndLoss;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class BiggestWinValue implements Function<Iterable<Position>, BigDecimal> {
	public BigDecimal apply(Iterable<Position> positions) {
		Iterable<BigDecimal> profitAndLoss = Iterables.transform(positions, new ProfitAndLoss());
		return Collections.max(Lists.newArrayList(profitAndLoss));
	}
}
