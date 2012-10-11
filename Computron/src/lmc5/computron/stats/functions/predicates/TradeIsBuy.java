package lmc5.computron.stats.functions.predicates;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.common.constants.TradeConstants.Side;

import com.google.common.base.Predicate;

public class TradeIsBuy implements Predicate<Trade> {
	public boolean apply(Trade trade) {
		return trade.getSide() == Side.BUY;
	}
}
