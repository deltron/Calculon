package lmc5.computron.stats.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.common.constants.TradeConstants;
import lmc5.computron.stats.domain.Position;

import org.apache.log4j.Logger;

public class PositionProcessor {

	/**
	 * TODO
	 * 
	 * Got rid of security class for now
	 * 
	 * Keyed only to symbol
	 * 
	 */
	Map<String, Position> openPositions = new HashMap<String, Position>();

	List<Position> positions = new ArrayList<Position>();

	private transient static final Logger log = Logger.getLogger(PositionProcessor.class);

	/**
	 * Populate list of positions from transactions
	 * 
	 * @return
	 */
	public List<Position> processTrades(Collection<Trade> trades) {
		ArrayList<Trade> sortedTrades = new ArrayList<Trade>(trades);
		// sort in date order
		Collections.sort(sortedTrades, new Comparator<Trade>() {
			public int compare(Trade o1, Trade o2) {
				// sort by date first
				int dateOrder = o1.getDt().compareTo(o2.getDt());

				// same date, sort by side (buys before sells)
				if (dateOrder == 0) {
					// works only for long only systems
					if (o1.getSide() == TradeConstants.Side.BUY && o2.getSide() == TradeConstants.Side.SELL)
						return -1;
					else if (o1.getSide() == TradeConstants.Side.SELL && o2.getSide() == TradeConstants.Side.BUY)
						return 1;
					else
						return 0;

				} else
					return dateOrder;
			}
		});

		for (Trade trade : sortedTrades) {
			log.debug("processing trade:" + trade);
			processTrade(trade);
		}

		return positions;
	}

	private void processTrade(Trade trade) {
		Position openPosition = getCurrentOpenPositionForSecurity(trade.getSymbol());
		if (openPosition == null) {
			// create a new position
			Position newPosition = new Position(trade);
			log.debug("new position:" + newPosition);
			positions.add(newPosition);
			openPositions.put(trade.getSymbol(), newPosition);
			log.debug(openPositions);
		} else {
			openPosition.addTrade(trade);
			// if position is flat, remove it from openPositions
			if (Position.Direction.NEUTRAL.equals(openPosition.getCurrentDirection())) {
				openPositions.remove(trade.getSymbol());
				log.debug("close position:" + openPosition);
			}
		}
	}

	/**
	 * returns the current position (last one) returns null if no positions are
	 * currently openned a FLAT position is considered closed
	 */
	public Position getCurrentOpenPositionForSecurity(String security) {
		return openPositions.get(security);
	}

}
