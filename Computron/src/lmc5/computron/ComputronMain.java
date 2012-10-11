package lmc5.computron;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lmc5.computron.common.config.TradeSystemConfig;
import lmc5.computron.common.systems.TradeSystem;
import lmc5.computron.optimizer.OptimizeRunner;
import lmc5.computron.stats.Statser;
import lmc5.computron.stats.TradingSystemStatistics;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class ComputronMain {
	public static final String PROPERTIES_EXTENSION = ".properties";
	private static final String computronConfigFile = "computron" + PROPERTIES_EXTENSION;
	private static final String configClassPrefix = "lmc5.computron.systems.";

	private static Logger LOGGER = Logger.getLogger(ComputronMain.class);

	public static void main(String[] args) {
		List<TradeSystemConfig> systemList = getConfig();
		for (TradeSystemConfig systemConfig : systemList) {
			LOGGER.info("----------------------------------------");
			LOGGER.info("Running trade system : " + systemConfig.getDescription());

			if (systemConfig.isOptimization()) {
				// optimization run
				OptimizeRunner runner = new OptimizeRunner(systemConfig.getProblem());
				runner.run();
			} else {
				// fixed parameter run
				TradeSystem system = systemConfig.getSystem();
				system.run();
				// calculate and log stats
				TradingSystemStatistics stats = Statser.calculateStats(system);
			}
		}
	}

	/**
	 * Load configurations
	 */
	public static List<TradeSystemConfig> getConfig() {
		LOGGER.info("Loading trade system configurations :");
		List<TradeSystemConfig> systems = new ArrayList<TradeSystemConfig>();

		try {
			PropertiesConfiguration computronConfig = new PropertiesConfiguration();
			computronConfig.setDelimiterParsingDisabled(true);
			computronConfig.load(computronConfigFile);

			Iterator keys = computronConfig.getKeys();
			while (keys.hasNext()) {
				String systemConfigProperties = (String) keys.next();
				String systemConfigClass = computronConfig.getString(systemConfigProperties);

				LOGGER.info(systemConfigClass + "(" + systemConfigProperties + ")");

				TradeSystemConfig systemConfig = (TradeSystemConfig) Class.forName(configClassPrefix + systemConfigClass).newInstance();
				systemConfig.loadFile(systemConfigProperties + PROPERTIES_EXTENSION);

				systems.add(systemConfig);
			}
		} catch (ConfigurationException e) {
			LOGGER.error("Error initializing Computron properties", e);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Error initializing Computron properties, could not find Config class", e);
		} catch (InstantiationException e) {
			LOGGER.error("Error initializing Computron properties, could not instanciate Config class", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("Error initializing Computron properties", e);
		}

		return systems;
	}
}
