package lmc5.computron.stats.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.common.constants.TradeConstants.Side;
import lmc5.computron.stats.functions.positions.single.AverageEntryPrice;
import lmc5.computron.stats.functions.positions.single.AverageExitPrice;
import lmc5.computron.stats.functions.positions.single.NetShareAmount;
import lmc5.computron.stats.functions.positions.single.ProfitAndLoss;
import lmc5.computron.stats.functions.predicates.PositionIsClosed;

import org.apache.log4j.Logger;

/**
 * Track a single position position is composed of
 * <li> multiple transactions
 * <li> stop policy
 * 
 */
public class Position {

	/** logger */
	private transient static final Logger log = Logger.getLogger(Position.class);

	/** position id */
	Integer positionId;

	/**
	 * Long : favorable to price increase
	 * 
	 * Short : favorable to price decrease
	 * 
	 * Neutral : market neutral
	 */
	public static enum Direction {
		LONG, SHORT, NEUTRAL
	};

	private List<Trade> trades = new ArrayList<Trade>();

	/** stop when position is openned */
	private BigDecimal initialStop;

	private Side entrySide;

	private String symbol;

	/**
	 * open position from trade
	 */
	public Position(Trade trade) {
		this.initialStop = trade.getStop_price();
		if (initialStop == null || initialStop.signum() < 0) {
			throw new RuntimeException("Can't open position with null or negative stop price");
		}

		trades.add(trade);
		this.entrySide = trade.getSide();
		this.symbol = trade.getSymbol();
	}

	/**
	 * add more to an already openned position
	 */
	public void addTrade(Trade trade) {
		trades.add(trade);
	}

	/**
	 * get the side from the transactions
	 */
	public Direction getCurrentDirection() {
		BigDecimal netShares = new NetShareAmount().apply(this).abs();

		if (netShares.signum() > 0) {
			return Direction.LONG;
		} else if (netShares.signum() < 0) {
			return Direction.SHORT;
		} else {
			// amount == 0
			return Direction.NEUTRAL;
		}
	}

	/**
	 * string representation
	 */
	public String toString() {
		String s = getCurrentDirection() + " " + symbol + " ENTER @ " + new AverageEntryPrice().apply(this);
		if (new PositionIsClosed().apply(this)) {
			s += " EXIT @ " + new AverageExitPrice().apply(this) + " PNL = " + new ProfitAndLoss().apply(this);
		}
		return s;
	}

	/**
	 * return a copy of this position's trades
	 * 
	 */
	public List<Trade> getTrades() {
		return trades;
	}

	public Side getEntrySide() {
		return entrySide;
	}
}
