package lmc5.computron.stats.functions.filters;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.predicates.PositionIsClosed;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class SelectClosedPositions implements Function<Iterable<Position>, Iterable<Position>> {
	public Iterable<Position> apply(Iterable<Position> positions) {
		return Iterables.filter(positions, new PositionIsClosed());
	}
}
