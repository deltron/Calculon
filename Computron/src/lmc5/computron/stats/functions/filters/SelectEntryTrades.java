package lmc5.computron.stats.functions.filters;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.common.constants.TradeConstants.Side;
import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.predicates.TradeIsBuy;
import lmc5.computron.stats.functions.predicates.TradeIsSell;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class SelectEntryTrades implements Function<Position, Iterable<Trade>> {
	public Iterable<Trade> apply(Position position) {
		Predicate<Trade> sidePredicate = null;

		if (position.getEntrySide() == Side.BUY)
			sidePredicate = new TradeIsBuy();

		if (position.getEntrySide() == Side.SELL)
			sidePredicate = new TradeIsSell();

		return Iterables.filter(position.getTrades(), sidePredicate);
	}
}
