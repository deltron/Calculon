package lmc5.computron.stoploss.percentprice;

import java.math.BigDecimal;

import lmc5.computron.stoploss.StopLossConfig;

import org.apache.commons.configuration.Configuration;

public class PercentPriceConfig implements StopLossConfig {
	private BigDecimal percentage;

	public void initWithConfig(Configuration config) {
		percentage = config.getBigDecimal(PercentPriceConstants.PERCENTAGE);
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

}
