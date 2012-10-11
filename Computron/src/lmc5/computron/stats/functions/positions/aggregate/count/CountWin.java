/*
 * Created on May 15, 2008
 */
package lmc5.computron.stats.functions.positions.aggregate.count;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectWinningPositions;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

/**
 * Count winning positions.
 * 
 * This excludes positions with 0 profit, they are counted as losing.
 */
public class CountWin implements Function<Iterable<Position>, BigDecimal> {

	public BigDecimal apply(Iterable<Position> positions) {
		return new BigDecimal(Iterables.size(new SelectWinningPositions().apply(positions)));
	}

}
