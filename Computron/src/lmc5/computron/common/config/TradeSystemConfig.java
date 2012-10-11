package lmc5.computron.common.config;

import lmc5.computron.ComputronMain;
import lmc5.computron.common.constants.TradeSystemConfigConstants;
import lmc5.computron.common.systems.TradeSystem;
import lmc5.computron.optimizer.problems.TradeSystemProblem;
import lmc5.computron.stoploss.StopLossPolicy;
import lmc5.computron.systems.gapper.config.GapperConfig;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public abstract class TradeSystemConfig {
	private static Logger LOGGER = Logger.getLogger(GapperConfig.class);

	protected PropertiesConfiguration config;
	protected TradeSystem system;
	protected TradeSystemProblem problem;
	protected StopLossPolicy stopLossPolicy;

	protected String description;
	protected String propertiesFileName;
	protected Boolean optimization;

	public void loadFile(String propertiesFile) {
		try {
			propertiesFileName = propertiesFile.substring(0, propertiesFile.lastIndexOf(ComputronMain.PROPERTIES_EXTENSION));

			config = new PropertiesConfiguration();
			config.setDelimiterParsingDisabled(true);
			config.load(propertiesFile);
		} catch (ConfigurationException e) {
			LOGGER.error("Problem loading properties file : " + propertiesFile, e);
		}

		initGeneralProperties();
		initSystemSpecificProperties();

		system.setConfig(this);
		if (problem != null)
			problem.setConfig(this);
	}

	private void initGeneralProperties() {
		try {
			String systemClassName = config.getString(TradeSystemConfigConstants.TRADE_SYSTEM_CLASS);
			this.system = (TradeSystem) Class.forName(systemClassName).newInstance();

			String stopLossClassName = config.getString(TradeSystemConfigConstants.STOPLOSS_POLICY_CLASS);
			this.stopLossPolicy = (StopLossPolicy) Class.forName(stopLossClassName).newInstance();
			this.stopLossPolicy.getConfig().initWithConfig(config);		
			
			this.description = config.getString(TradeSystemConfigConstants.TRADE_SYSTEM_DESCRIPTION);
			this.optimization = config.getBoolean(TradeSystemConfigConstants.TRADE_SYSTEM_OPTIMIZATION, false);
						
			// load the problem class if it's an optimization run
			if (optimization) {
				String problemClassName = config.getString(TradeSystemConfigConstants.TRADE_SYSTEM_OPTIMIZATION_PROBLEM_CLASS);
				this.problem = (TradeSystemProblem) Class.forName(problemClassName).newInstance();
			}
		} catch (InstantiationException e) {
			LOGGER.error("Problem init system class", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("Problem init system class", e);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Problem init system class", e);
		}

	}

	/**
	 * Read in Specific Trade System Configuration
	 * 
	 */
	public abstract void initSystemSpecificProperties();

	public Configuration getConfig() {
		return config;
	}

	public TradeSystem getSystem() {
		return system;
	}

	public Boolean isOptimization() {
		return optimization;
	}

	public TradeSystemProblem getProblem() {
		return problem;
	}

	public String getDescription() {
		return description;
	}

	public String getPropertiesFileName() {
		return propertiesFileName;
	}
	
	public StopLossPolicy getStopLossPolicy() {
		return stopLossPolicy;
	}
	
	public void setStopLossPolicy(StopLossPolicy stopLossPolicy) {
		this.stopLossPolicy = stopLossPolicy;
	}
}
