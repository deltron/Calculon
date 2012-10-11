package lmc5.computron.systems.gapper.config;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import lmc5.computron.common.config.DataSourceConfig;
import lmc5.computron.common.config.ExecutorConfig;
import lmc5.computron.common.config.TradeSystemConfig;
import lmc5.computron.common.constants.DataSourceConstants;
import lmc5.computron.common.constants.ExecutorConstants;
import lmc5.computron.stats.util.FormatUtil;
import lmc5.computron.systems.gapper.util.GapperConstants;

import org.apache.log4j.Logger;

public class GapperConfig extends TradeSystemConfig implements DataSourceConfig, ExecutorConfig {
	private static Logger LOGGER = Logger.getLogger(GapperConfig.class);

	private BigDecimal gapSizePct;
	private Integer minVolume;
	private BigDecimal minPrice;
	private Integer expiration;
	private Date startDate;
	private Date endDate;
	private String systemName;
	private Integer gapMin;
	private Integer gapMax;
	private Integer stopMin;
	private Integer stopMax;

	public void initSystemSpecificProperties() {
		try {
			startDate = FormatUtil.YMD_DATE_FORMAT.parse(config.getString(GapperConstants.TRADE_START_DATE));
			endDate =  FormatUtil.YMD_DATE_FORMAT.parse(config.getString(GapperConstants.TRADE_END_DATE));
		} catch (ParseException e) {
			LOGGER.error("Unable to parse start or end date", e);
		}

		minVolume = config.getInt(GapperConstants.TRADE_MIN_VOLUME);
		minPrice = config.getBigDecimal(GapperConstants.TRADE_MIN_PRICE);
		expiration = config.getInt(GapperConstants.TRADE_EXPIRATION);
		
		if (isOptimization()) {
			gapMin = config.getInt(GapperConstants.TRADE_GAP_MIN);
			gapMax = config.getInt(GapperConstants.TRADE_GAP_MAX);

			stopMin = config.getInt(GapperConstants.TRADE_STOP_MIN);
			stopMax = config.getInt(GapperConstants.TRADE_STOP_MAX);
		} else {
			gapSizePct = BigDecimal.valueOf(config.getDouble(GapperConstants.TRADE_GAP) / 100.0);
			
			// gap should be down
			if (gapSizePct.signum() >= 0)
				gapSizePct = gapSizePct.negate();
		}
	}

	public String getDriver() {
		return getConfig().getString(DataSourceConstants.DB_DRIVER);
	}

	public String getPassword() {
		return getConfig().getString(DataSourceConstants.DB_PASSWORD);
	}

	public Integer getPoolMax() {
		return getConfig().getInt(DataSourceConstants.DB_POOLMAX);
	}

	public Integer getPoolMin() {
		return getConfig().getInt(DataSourceConstants.DB_POOLMIN);
	}

	public String getURL() {
		return getConfig().getString(DataSourceConstants.DB_URL);
	}

	public String getUser() {
		return getConfig().getString(DataSourceConstants.DB_USER);
	}

	public Integer getMaxActiveThreads() {
		return getConfig().getInt(ExecutorConstants.MAX_TASK);
	}

	public Integer getWaitQueueSize() {
		return getConfig().getInt(ExecutorConstants.MAX_QUEUE);
	}

	public BigDecimal getGapSizePct() {
		return gapSizePct;
	}

	public void setGapSizePct(BigDecimal gapSizePct) {
		this.gapSizePct = gapSizePct;
	}

	public Integer getMinVolume() {
		return minVolume;
	}

	public void setMinVolume(Integer minVolume) {
		this.minVolume = minVolume;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public Integer getExpiration() {
		return expiration;
	}

	public void setExpiration(Integer stopAfterDays) {
		this.expiration = stopAfterDays;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Integer getGapMin() {
		return gapMin;
	}

	public void setGapMin(Integer gapMin) {
		this.gapMin = gapMin;
	}

	public Integer getGapMax() {
		return gapMax;
	}

	public void setGapMax(Integer gapMax) {
		this.gapMax = gapMax;
	}

	public Integer getStopMin() {
		return stopMin;
	}

	public void setStopMin(Integer stopMin) {
		this.stopMin = stopMin;
	}

	public Integer getStopMax() {
		return stopMax;
	}

	public void setStopMax(Integer stopMax) {
		this.stopMax = stopMax;
	}

}
