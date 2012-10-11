package lmc5.computron.stats.functions.filters;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.predicates.PositionIsClosed;
import lmc5.computron.stats.functions.predicates.PositionIsWinning;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

public class SelectLosingPositions implements Function<Iterable<Position>, Iterable<Position>> {
	public Iterable<Position> apply(Iterable<Position> positions) {
		Predicate<Position> notWinning = Predicates.not(new PositionIsWinning());
		Predicate<Position> losingAndClosed = Predicates.and(new PositionIsClosed(), notWinning);
		return Iterables.filter(positions, losingAndClosed);
	}
}
