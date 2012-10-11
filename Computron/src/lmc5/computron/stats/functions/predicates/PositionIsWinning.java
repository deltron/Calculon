/*
 * Created on May 15, 2008
 */
package lmc5.computron.stats.functions.predicates;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.positions.single.ProfitAndLoss;

import com.google.common.base.Predicate;

public class PositionIsWinning implements Predicate<Position> {

	public boolean apply(Position position) {
		return new ProfitAndLoss().apply(position).signum() > 0;
	}

}
