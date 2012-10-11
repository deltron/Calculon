package lmc5.computron.optimizer.problems;

import lmc5.computron.common.config.TradeSystemConfig;

public abstract class TradeSystemProblem extends GeneralProblem {
	public abstract void setConfig(TradeSystemConfig config);
	
	public abstract TradeSystemConfig getConfig();
}
