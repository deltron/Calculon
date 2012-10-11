package lmc5.computron.stats;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.filters.SelectClosedPositions;
import lmc5.computron.stats.functions.positions.aggregate.average.AverageLossValue;
import lmc5.computron.stats.functions.positions.aggregate.average.AverageProfitAndLoss;
import lmc5.computron.stats.functions.positions.aggregate.average.AverageWinValue;
import lmc5.computron.stats.functions.positions.aggregate.average.Expectancy;
import lmc5.computron.stats.functions.positions.aggregate.average.KellyCriterion;
import lmc5.computron.stats.functions.positions.aggregate.count.CountClosed;
import lmc5.computron.stats.functions.positions.aggregate.count.CountLose;
import lmc5.computron.stats.functions.positions.aggregate.count.CountWin;
import lmc5.computron.stats.functions.positions.aggregate.count.PercentLose;
import lmc5.computron.stats.functions.positions.aggregate.count.PercentWin;
import lmc5.computron.stats.functions.positions.aggregate.max.BiggestLossValue;
import lmc5.computron.stats.functions.positions.aggregate.max.BiggestWinValue;
import lmc5.computron.stats.functions.positions.aggregate.total.TotalLossValue;
import lmc5.computron.stats.functions.positions.aggregate.total.TotalProfitAndLoss;
import lmc5.computron.stats.functions.positions.aggregate.total.TotalR;
import lmc5.computron.stats.functions.positions.aggregate.total.TotalWinValue;

import org.apache.log4j.Logger;

/**
 * Statistics calculated on lists of positions
 * 
 */
public class TradingSystemStatistics {
	protected static final transient Logger log = Logger.getLogger(TradingSystemStatistics.class);

	private Iterable<Position> positions = new ArrayList<Position>();

	public static enum StatGroup {
		COUNT, AVERAGE, TOTAL, MAXIMUM, RISK
	}

	/**
	 * Counters
	 */
	public static enum Count {
		CLOSED, WIN, LOSE, PERCENT_WIN, PERCENT_LOSE
	}

	/**
	 * profit and loss - average
	 */
	public static enum Average {
		PROFIT_AND_LOSS, WIN_VALUE, LOSS_VALUE
	}

	/**
	 * profit and loss - total
	 */
	public static enum Total {
		PROFIT_AND_LOSS, WIN_VALUE, LOSS_VALUE
	}

	/**
	 * profit and loss - max
	 */
	public static enum Maximum {
		WIN, LOSS
	}

	/**
	 * Risk measures
	 */
	public static enum Risk {
		TOTAL_R, EXPECTANCY, KELLY
	};

	private Map<StatGroup, Map<?, BigDecimal>> stats = new HashMap<StatGroup, Map<?, BigDecimal>>();

	/**
	 * constructor
	 */
	public TradingSystemStatistics(Collection<Position> positions) {
		// do only closed positions 
		this.positions = new SelectClosedPositions().apply(positions);
		
		// only calculate if there are positions
		if (positions.size() > 0) {
			// break this out into a setup/calculation like it was?
			doCount();
			doMaximum();
			doRisk();
			doTotal();
			doAverage();

			for (StatGroup group : stats.keySet()) {
				log.info(group + " " + stats.get(group));
			}
		}
	}

	private void doMaximum() {
		Map<Maximum, BigDecimal> maximums = new HashMap<Maximum, BigDecimal>();

		maximums.put(Maximum.WIN, new BiggestWinValue().apply(positions));
		maximums.put(Maximum.LOSS, new BiggestLossValue().apply(positions));

		stats.put(StatGroup.MAXIMUM, maximums);
	}

	private void doAverage() {
		Map<Average, BigDecimal> averages = new HashMap<Average, BigDecimal>();

		averages.put(Average.PROFIT_AND_LOSS, new AverageProfitAndLoss().apply(positions));
		averages.put(Average.WIN_VALUE, new AverageWinValue().apply(positions));
		averages.put(Average.LOSS_VALUE, new AverageLossValue().apply(positions));

		stats.put(StatGroup.AVERAGE, averages);
	}

	private void doRisk() {
		Map<Risk, BigDecimal> risks = new HashMap<Risk, BigDecimal>();

		risks.put(Risk.TOTAL_R, new TotalR().apply(positions));
		risks.put(Risk.EXPECTANCY, new Expectancy().apply(positions));
		risks.put(Risk.KELLY, new KellyCriterion().apply(positions));

		stats.put(StatGroup.RISK, risks);
	}

	private void doTotal() {
		Map<Total, BigDecimal> totals = new HashMap<Total, BigDecimal>();

		totals.put(Total.PROFIT_AND_LOSS, new TotalProfitAndLoss().apply(positions));
		totals.put(Total.WIN_VALUE, new TotalWinValue().apply(positions));
		totals.put(Total.LOSS_VALUE, new TotalLossValue().apply(positions));

		stats.put(StatGroup.TOTAL, totals);
	}

	private void doCount() {
		Map<Count, BigDecimal> counts = new HashMap<Count, BigDecimal>();

		counts.put(Count.CLOSED, new CountClosed().apply(positions));
		counts.put(Count.WIN, new CountWin().apply(positions));
		counts.put(Count.LOSE, new CountLose().apply(positions));
		counts.put(Count.PERCENT_WIN, new PercentWin().apply(positions));
		counts.put(Count.PERCENT_LOSE, new PercentLose().apply(positions));

		stats.put(StatGroup.COUNT, counts);
	}

	public Map<StatGroup, Map<?, BigDecimal>> getStats() {
		return stats;
	}
}
