/*
 * Created on May 15, 2008
 */
package lmc5.computron.stats.functions.positions.aggregate.count;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectClosedPositions;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class CountClosed implements Function<Iterable<Position>, BigDecimal> {

	public BigDecimal apply(Iterable<Position> positions) {
		return new BigDecimal(Iterables.size(new SelectClosedPositions().apply(positions)));
	}

}
