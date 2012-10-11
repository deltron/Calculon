package lmc5.computron.stats.functions.util;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.common.constants.TradeConstants.Side;

public class ValidationUtil {
	public static Boolean verifySameSide(Iterable<Trade> trades) {
		Side side = trades.iterator().next().getSide();
		for (Trade trade : trades) {
			if (trade.getSide() != side)
				return false;
		}
		return true;
	}
}
