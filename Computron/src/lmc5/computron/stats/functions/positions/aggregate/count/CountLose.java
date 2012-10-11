/*
 * Created on May 15, 2008
 */
package lmc5.computron.stats.functions.positions.aggregate.count;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectLosingPositions;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

/**
 * Count losing positions by negating winning positions.
 * 
 * This includes positions with 0 profit.
 */
public class CountLose implements Function<Iterable<Position>, BigDecimal> {

	public BigDecimal apply(Iterable<Position> positions) {
		return new BigDecimal(Iterables.size(new SelectLosingPositions().apply(positions)));
	}

}
