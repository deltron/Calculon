package lmc5.computron.common.systems;

import java.util.Collection;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.common.config.TradeSystemConfig;

public interface TradeSystem {
	public void run();

	public Collection<Trade> getTrades();

	public void setConfig(TradeSystemConfig systemConfig);

}
