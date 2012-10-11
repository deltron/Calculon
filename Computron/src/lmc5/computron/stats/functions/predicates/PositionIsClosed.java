/*
 * Created on May 15, 2008
 */
package lmc5.computron.stats.functions.predicates;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.positions.single.NetShareAmount;

import com.google.common.base.Predicate;

/**
 * Position is closed if the Net Share amount (buys - sells) is zero.
 * 
 */
public class PositionIsClosed implements Predicate<Position> {

	public boolean apply(Position position) {
		return new NetShareAmount().apply(position).signum() == 0;
	}

}
