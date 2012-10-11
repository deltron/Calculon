package lmc5.computron.systems.gapper;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

import javax.sql.DataSource;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.common.config.TradeSystemConfig;
import lmc5.computron.common.systems.TradeSystem;
import lmc5.computron.common.util.DataSourceUtil;
import lmc5.computron.common.util.ExecutorUtil;
import lmc5.computron.stats.util.FormatUtil;
import lmc5.computron.systems.gapper.beans.GapData;
import lmc5.computron.systems.gapper.config.GapperConfig;
import lmc5.computron.systems.gapper.tasks.GapperTask;
import lmc5.computron.systems.gapper.util.GapperConstants;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

public class Gapper implements TradeSystem {
	private static final Logger LOGGER = Logger.getLogger(Gapper.class);
	private GapperConfig config;

	private ExecutorService executor;
	private CompletionService<Collection<Trade>> ecs;

	private DataSource dataSource;

	private Long taskCount = 0l;
	private Collection<Trade> trades = new HashSet<Trade>();

	private Long startTaskTime = System.currentTimeMillis();

	public void setConfig(TradeSystemConfig systemConfig) {
		if (systemConfig instanceof GapperConfig) {
			GapperConfig gapperConfig = (GapperConfig) systemConfig;

			// initialize everything
			this.config = gapperConfig;

			this.executor = ExecutorUtil.setupExecutor(config);
			this.dataSource = DataSourceUtil.setupDataSource(config);
			this.ecs = new ExecutorCompletionService<Collection<Trade>>(executor);
		} else {
			LOGGER.error("Gapper received a non-gapper configuration!");
		}
	}

	public void run() {
		try {
			LOGGER.info("Stop Loss  : " + config.getStopLossPolicy().toString());
			LOGGER.info("Gap Size   : " + config.getGapSizePct());
			LOGGER.info("Expiration : " + config.getExpiration());
			LOGGER.info("Start date : " + FormatUtil.YMD_DATE_FORMAT.format(config.getStartDate()));
			LOGGER.info("End date   : " + FormatUtil.YMD_DATE_FORMAT.format(config.getEndDate()));

			// Run the initial screen
			// will also launch gapper tasks to process results
			doInitialQuery();

			// Get all the resulting trades from the executor
			for (int i = 0; i < taskCount; i++)
				trades.addAll(ecs.take().get());

			// count how long all this took
			Long endTime = System.currentTimeMillis();
			Long runTime = endTime - startTaskTime;
			Long runTimeSeconds = runTime / 1000;
			Long tradesPerSecond = 0l;

			if (runTimeSeconds > 0)
				tradesPerSecond = taskCount / runTimeSeconds;

			LOGGER.info(trades.size() + " trades generated in " + runTime + "ms (" + tradesPerSecond + " gap/sec)");
		} catch (ExecutionException e) {
			LOGGER.error("Error", e);
		} catch (InterruptedException e) {
			LOGGER.error("Error", e);
		} finally {

			// cleanly shutdown the executor
			executor.shutdown();
		}
	}

	/**
	 * Run the initial filter query, looking for gaps that match the config
	 * parameters
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void doInitialQuery() throws InterruptedException {
		LOGGER.info("Looking for gaps");
		Long startTime = System.currentTimeMillis();

		String query = config.getConfig().getString(GapperConstants.INITIAL_SCREEN_QUERY);

		QueryRunner run = new QueryRunner(dataSource);

		try {
			// Run the query
			Object[] params = new Object[] { new java.sql.Date(config.getStartDate().getTime()), new java.sql.Date(config.getEndDate().getTime()), config.getMinPrice(),
					config.getMinVolume(), config.getGapSizePct() };
			List<GapData> results = (List<GapData>) run.query(query, params, new BeanListHandler(GapData.class));

			// Stop the query clock, start task clock
			Long endTime = startTaskTime = System.currentTimeMillis();
			LOGGER.info("Found " + results.size() + " gap events in " + (endTime - startTime) + "ms,  processing...");

			// send each result to a GapperTask for further processing
			for (GapData gapData : results) {
				gapData.setPrev_close(gapData.getOpen().subtract(gapData.getDrift()));

				ecs.submit(new GapperTask(this, gapData));
				taskCount++;
			}
		} catch (SQLException e) {
			if (e.getMessage().matches(".*Query did not produce a result set.*")) {
				LOGGER.info("No results found.");
				// do nothing, this is an excpetion thrown by the MonetDB driver
				// when there are no results for a query. It's not an
				// exception-worthy problem.
			} else {
				LOGGER.error("exception", e);
			}
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public Collection<Trade> getTrades() {
		return trades;
	}

	public GapperConfig getConfig() {
		return config;
	}
}
