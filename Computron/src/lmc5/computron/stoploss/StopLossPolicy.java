package lmc5.computron.stoploss;

import java.math.BigDecimal;

import lmc5.computron.common.beans.Trade;

public interface StopLossPolicy {
	public BigDecimal getStopLoss(Trade openTrade);
	public StopLossConfig getConfig();
	public void setConfig(StopLossConfig config);
}
