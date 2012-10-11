package lmc5.computron.stats.util;

import lmc5.computron.common.constants.TradeConstants.Side;

import org.apache.log4j.Logger;

public class TradeUtil {
	private static final Logger LOGGER = Logger.getLogger(TradeUtil.class);

	public static Side oppositeSide(Side side) {
		if (side == Side.BUY)
			return Side.SELL;
		else if (side == Side.SELL)
			return Side.BUY;
		else
			LOGGER.error("Side was not BUY or SELL : " + side);

		return null;
	}
}
