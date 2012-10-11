/*
 * Created on May 9, 2008
 */
package lmc5.computron.stats;

import java.util.List;

import lmc5.computron.common.systems.TradeSystem;
import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.processor.PositionProcessor;

import org.apache.log4j.Logger;

/**
 * Statser is a trading statistics calculation machine input: list of trades
 * (system) output: list of stats
 * 
 * It creates a list of positions and the equity curve internaly.
 * 
 * @author phil
 */
public class Statser {

	private static final Logger log = Logger.getLogger(Statser.class);;

	/**
	 * static method to calculate stats from a collection of trades.
	 */
	public static TradingSystemStatistics runSystemAndCalculateStats(TradeSystem system) {
		system.run();
		return calculateStats(system);
	}

	/**
	 * calculate and return stats on a system that has already been run.
	 */
	public static TradingSystemStatistics calculateStats(TradeSystem system) {
		// create positions
		PositionProcessor processor = new PositionProcessor();
		List<Position> positions = processor.processTrades(system.getTrades());

		// create stats
		TradingSystemStatistics stats = new TradingSystemStatistics(positions);
		return stats;
	}
}
