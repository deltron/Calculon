package lmc5.computron.systems.gapper.tasks;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.common.constants.TradeConstants;
import lmc5.computron.stats.util.FormatUtil;
import lmc5.computron.systems.gapper.Gapper;
import lmc5.computron.systems.gapper.beans.GapData;
import lmc5.computron.systems.gapper.util.GapperConstants;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

public class GapperTask implements Callable<Collection<Trade>> {
	private static Logger LOGGER = Logger.getLogger(GapperTask.class);

	private String gapCloseQuery = null;
	private String interimPricesQuery = null;

	private final GapData gapData;
	private final Gapper gapper;

	private Map<Date, BigDecimal> closePrices = new HashMap<Date, BigDecimal>();
	private Map<Date, BigDecimal> lowPrices = new HashMap<Date, BigDecimal>();
	private Map<Date, BigDecimal> openPrices = new HashMap<Date, BigDecimal>();

	private Trade openTrade;
	private Trade closeTrade;

	public GapperTask(Gapper gapper, final GapData gapData) {
		gapCloseQuery = gapper.getConfig().getConfig().getString(GapperConstants.FIND_GAP_CLOSE_QUERY);
		interimPricesQuery = gapper.getConfig().getConfig().getString(GapperConstants.FIND_INTERIM_PRICES_QUERY);

		this.gapData = gapData;
		this.gapper = gapper;
	}

	/**
	 * Main entry point for the GapperTask
	 * 
	 */
	public Collection<Trade> call() {
		Date gapCloseDate = findGapClose();
		findPrices(gapCloseDate);
		doTrade();

		Set<Trade> trades = new HashSet<Trade>();

		if (openTrade != null)
			trades.add(openTrade);
		if (closeTrade != null)
			trades.add(closeTrade);

		return trades;
	}

	/**
	 * Run through the prices and and apply the closing logic each day
	 * 
	 */
	private void doTrade() {
		openTrade();

		int daysOpen = 0;
		for (Date date : getOrderedDates()) {
			boolean positionIsClosed = closePosition(date, daysOpen);

			if (positionIsClosed)
				break; // stop processing prices
			else
				daysOpen++; // increment day counter
		}
	}

	/**
	 * Try to close the position (stop loss, gap filled, time expired)
	 * 
	 */
	private boolean closePosition(final Date date, final int daysOpen) {
		BigDecimal closePrice = closePrices.get(date);
		BigDecimal lowPrice = lowPrices.get(date);
		BigDecimal openPrice = openPrices.get(date);

		if (lowPrice.compareTo(openTrade.getStop_price()) <= 0) {
			// stopped out : low of the day hit the stop loss while trading
			// assume exit price is equal to calculated stop loss

			closeTrade(date, openTrade.getStop_price(), TradeConstants.Status.STOP_LOSS);
			return true;
		} else if (openPrice.compareTo(openTrade.getStop_price()) <= 0) {
			// stopped out open price gapped below stop loss
			// assume exit price is equal to the open price

			closeTrade(date, openPrice, TradeConstants.Status.STOP_LOSS);
			return true;
		} else if (closePrice.compareTo(gapData.getPrev_close()) >= 0) {
			// gap filled, assume exit price is the close of the day
			BigDecimal exitPrice = closePrice;

			closeTrade(date, exitPrice, TradeConstants.Status.NORMAL_CLOSE);
			return true;
		} else if (daysOpen >= gapper.getConfig().getExpiration().intValue()) {
			// time expired, assume exit price is the close of the day
			BigDecimal exitPrice = closePrice;

			closeTrade(date, exitPrice, TradeConstants.Status.EXPIRED);
			return true;
		}

		return false;
	}

	/**
	 * define the closing trade
	 * 
	 * assumptions for now: shares is 1
	 * 
	 */
	private void closeTrade(final Date date, final BigDecimal exitPrice, final TradeConstants.Status status) {
		closeTrade = new Trade();
		closeTrade.setSide(TradeConstants.Side.SELL);
		closeTrade.setShares(1l);
		closeTrade.setSymbol(gapData.getSymbol());
		closeTrade.setExchange(gapData.getExchange());
		closeTrade.setDt(date);
		closeTrade.setPrice(exitPrice);
		closeTrade.setStatus(status);

		// set the open trade status to CLOSED
		openTrade.setStatus(TradeConstants.Status.CLOSED);

		LOGGER.debug("(" + gapData.getSymbol() + " " + gapData.getExchange() + " " + gapData.getDt() + ") " + closeTrade);
	}

	/**
	 * define the opening trade
	 * 
	 * assumptions for now: shares is 1
	 * 
	 */
	private void openTrade() {
		// create a position...
		openTrade = new Trade();
		openTrade.setSide(TradeConstants.Side.BUY);
		openTrade.setShares(1l);
		openTrade.setSymbol(gapData.getSymbol());
		openTrade.setExchange(gapData.getExchange());
		openTrade.setDt(gapData.getDt());
		openTrade.setPrice(gapData.getOpen());
		openTrade.setStatus(TradeConstants.Status.OPEN);
		
		BigDecimal stopPrice = gapper.getConfig().getStopLossPolicy().getStopLoss(openTrade);
		openTrade.setStop_price(stopPrice);
		
		LOGGER.debug("(" + gapData.getSymbol() + " " + gapData.getExchange() + " " + gapData.getDt() + ") " + openTrade);

		// quick sanity check
		if (openTrade.getStop_price() == null) {
			LOGGER.error("Generated a trade with no stop loss");
		}
	}

	/**
	 * Find the first day the gap closes, or null if never
	 * 
	 */
	private Date findGapClose() {
		LOGGER.debug("(" + gapData.getSymbol() + " " + gapData.getExchange() + " " + gapData.getDt() + ") Looking for gap close");
		QueryRunner run = new QueryRunner(gapper.getDataSource());
		Date gapCloseDate = adjustedEndDate(null);

		try {
			Object[] params = new Object[] { gapData.getSymbol(), gapData.getDt(), new java.sql.Date(gapCloseDate.getTime()), gapData.getPrev_close() };
			gapCloseDate = (Date) run.query(gapCloseQuery, params, new ScalarHandler());
		} catch (SQLException e) {
			if (e.getMessage().matches(".*Query did not produce a result set.*")) {
				// do nothing, this is an excpetion thrown by the MonetDB driver
				// when there are no results for a query. It's not an
				// exception-worthy problem.
			} else {
				LOGGER.error("exception", e);
			}
		}

		if (gapCloseDate != null)
			LOGGER.debug("(" + gapData.getSymbol() + " " + gapData.getExchange() + " " + gapData.getDt() + ") Found gap close on " + gapCloseDate);
		else
			LOGGER.debug("(" + gapData.getSymbol() + " " + gapData.getExchange() + " " + gapData.getDt() + ") Gap never closed");

		return gapCloseDate;
	}

	/**
	 * Find open and close prices from gap open to close date, or the end date
	 * whichever is sooner
	 * 
	 * Results are saved in two places:
	 * 
	 * 1) a list of prices ordered by date
	 * 
	 * 2) a map of date->closePrice
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void findPrices(final Date gapCloseDate) {
		QueryRunner run = new QueryRunner(gapper.getDataSource());
		try {
			Date endDate = adjustedEndDate(gapCloseDate);

			LOGGER.debug("(" + gapData.getSymbol() + " " + gapData.getExchange() + " " + gapData.getDt() + ") Looking for interim price up to "
					+ FormatUtil.YMD_DATE_FORMAT.format(endDate));

			Object[] params = new Object[] { gapData.getSymbol(), gapData.getDt(), new java.sql.Date(endDate.getTime()) };
			Map<Date, Map<String, BigDecimal>> results = (Map) run.query(interimPricesQuery, params, new KeyedHandler("DT"));

			for (Date dt : results.keySet())
				openPrices.put(dt, results.get(dt).get("OPEN"));

			for (Date dt : results.keySet())
				lowPrices.put(dt, results.get(dt).get("LOW"));

			for (Date dt : results.keySet())
				closePrices.put(dt, results.get(dt).get("CLOSE"));

		} catch (SQLException e) {
			if (e.getMessage().matches(".*Query did not produce a result set.*")) {
				// do nothing, this is an excpetion thrown by the MonetDB driver
				// when there are no results for a query. It's not an
				// exception-worthy problem.
			} else {
				LOGGER.error("exception", e);
			}
		}
	}

	/**
	 * Return a list of dates for the interim prices, in ascending order
	 * 
	 */
	private List<Date> getOrderedDates() {
		List<Date> dates = new ArrayList<Date>();
		dates.addAll(closePrices.keySet());

		Collections.sort(dates);

		return dates;
	}

	/**
	 * Returns a date that is either
	 * 
	 * 1) Gap close date
	 * 
	 * 2) gap open date + expiration_days
	 * 
	 * 3) end_date for system
	 * 
	 * Whichever is sooner, plus one week in case of holidays, etc.
	 * 
	 */
	private Date adjustedEndDate(Date gapCloseDate) {
		Date endDate = null;

		// choose gap close or expiration days
		if (gapCloseDate != null) {
			endDate = gapCloseDate;
		} else {
			Integer expiration = gapper.getConfig().getExpiration();
			Calendar expireCal = Calendar.getInstance();
			expireCal.setTime(gapData.getDt());
			expireCal.add(Calendar.DAY_OF_YEAR, expiration);
			endDate = expireCal.getTime();
		}

		// get an extra week of prices in case of holidays and weekends
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		endDate = cal.getTime();

		// gap close or expiration occurs outside scope
		if (endDate.compareTo(gapper.getConfig().getEndDate()) >= 0) {
			endDate = gapper.getConfig().getEndDate();
		}

		return endDate;
	}
}
