package lmc5.computron.systems.gapper.problem;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lmc5.computron.common.config.TradeSystemConfig;
import lmc5.computron.common.systems.TradeSystem;
import lmc5.computron.optimizer.dimension.IntegerDimension;
import lmc5.computron.optimizer.problems.TradeSystemProblem;
import lmc5.computron.optimizer.solution.GeneralSolution;
import lmc5.computron.stats.Statser;
import lmc5.computron.stats.TradingSystemStatistics;
import lmc5.computron.stats.TradingSystemStatistics.Count;
import lmc5.computron.stats.TradingSystemStatistics.Risk;
import lmc5.computron.stats.TradingSystemStatistics.StatGroup;
import lmc5.computron.stoploss.StopLossPolicy;
import lmc5.computron.stoploss.percentprice.PercentPriceConfig;
import lmc5.computron.stoploss.percentprice.PercentPriceStop;
import lmc5.computron.systems.gapper.Gapper;
import lmc5.computron.systems.gapper.config.GapperConfig;

import org.apache.log4j.Logger;

import com.oat.SolutionEvaluationException;

public class GapperProblem extends TradeSystemProblem {
	private static Logger LOGGER = Logger.getLogger(GapperProblem.class);
	private static int count = 0;

	private GapperConfig config;

	/**
	 * use integers to reduce the solution space
	 * 
	 * ie. don't really care about a gap size of 5.5 or 5.75, 5 and 6 is enough
	 * precision
	 */
	@Override
	protected void setupDimensions() {
		if (config != null) {
			dimensions.add(new IntegerDimension("gap", config.getGapMin(), config.getGapMax()));
			dimensions.add(new IntegerDimension("stopLoss", config.getStopMin(), config.getStopMax()));
		}
	}

	@Override
	public boolean isMinimization() {
		return false;
	}

	public String getName() {
		return "Gapper";
	}

	@Override
	protected double problemSpecificCost(GeneralSolution solution) throws SolutionEvaluationException {
		// coordinates generated from solution
		Double gap = solution.getCoordinate().get(0).doubleValue() / 100.0;
		Double stopLossCoordinate = solution.getCoordinate().get(1).doubleValue() / 100.0;

		// copy into a gapper configuration
		// Monet will complain if you don't round down BigDecimals to the
		// same precision as the database
		config.setGapSizePct(BigDecimal.valueOf(gap).setScale(6, RoundingMode.HALF_DOWN).negate());

		config.setStopLossPolicy(setupStopLoss(stopLossCoordinate));

		TradeSystem system = new Gapper();
		system.setConfig(config);

		TradingSystemStatistics stats = Statser.runSystemAndCalculateStats(system);
		
		Double cost = 0.0;
		if (!stats.getStats().isEmpty()) {
			BigDecimal expectancy = stats.getStats().get(StatGroup.RISK).get(Risk.EXPECTANCY);
			Integer positionCount = stats.getStats().get(StatGroup.COUNT).get(Count.CLOSED).intValue();
			cost = expectancy.doubleValue();

			count++;
			LOGGER.info(config.getPropertiesFileName() + " " + count + " " + gap + " " + stopLossCoordinate + " " + config.getExpiration() + " " + cost + " " + positionCount);
		}

		return cost;
	}

	private StopLossPolicy setupStopLoss(Double stopLoss) {
		PercentPriceConfig stopLossConfig = new PercentPriceConfig();
		stopLossConfig.setPercentage(BigDecimal.valueOf(stopLoss).setScale(6, RoundingMode.HALF_DOWN));

		StopLossPolicy stopLossPolicy = new PercentPriceStop();
		stopLossPolicy.setConfig(stopLossConfig);

		return stopLossPolicy;
	}

	@Override
	public TradeSystemConfig getConfig() {
		return config;
	}

	@Override
	public void setConfig(TradeSystemConfig systemConfig) {
		if (systemConfig instanceof GapperConfig) {
			this.config = (GapperConfig) systemConfig;
			setupDimensions();
		} else {
			LOGGER.error("GapperProblem received a non-gapper configuration");
		}
	}

}
