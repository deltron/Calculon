package lmc5.computron.stats.functions.filters;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.predicates.PositionIsClosed;
import lmc5.computron.stats.functions.predicates.PositionIsWinning;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

public class SelectWinningPositions implements Function<Iterable<Position>, Iterable<Position>> {
	public Iterable<Position> apply(Iterable<Position> positions) {
		Predicate<Position> winningAndClosed = Predicates.and(new PositionIsClosed(), new PositionIsWinning());
		return Iterables.filter(positions, winningAndClosed);
	}
}
