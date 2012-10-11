package lmc5.computron.stoploss.percentprice;

import java.math.BigDecimal;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.stoploss.StopLossConfig;
import lmc5.computron.stoploss.StopLossPolicy;

import org.apache.log4j.Logger;

/**
 * Simple stop policy set as a percent of price
 * 
 */
public class PercentPriceStop implements StopLossPolicy {
	private static Logger LOGGER = Logger.getLogger(PercentPriceStop.class);
	PercentPriceConfig config = new PercentPriceConfig();

	public BigDecimal getStopLoss(Trade openTrade) {
		BigDecimal distance = BigDecimal.ONE.subtract(config.getPercentage());
		return openTrade.getPrice().multiply(distance);
	}

	public StopLossConfig getConfig() {
		return config;
	}

	public void setConfig(StopLossConfig config) {
		if (config instanceof PercentPriceConfig)
			this.config = (PercentPriceConfig) config;
		else
			LOGGER.error("PercentPriceStop received a non-percentPrice config : " + config);
	}

	@Override
	public String toString() {
		return "Percent Price Stop with percentage = " + config.getPercentage();
	}
}
