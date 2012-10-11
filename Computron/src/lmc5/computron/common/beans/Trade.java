package lmc5.computron.common.beans;

import java.math.BigDecimal;
import java.util.Date;

import lmc5.computron.common.constants.TradeConstants;
import lmc5.computron.stats.util.FormatUtil;

public class Trade implements Comparable {

	private Long id;
	private Long trade_system_id;
	private Long settings_id;
	private Long position_id;
	private TradeConstants.Side side;
	private Long shares;
	private String symbol;
	private String exchange;
	private Date dt;
	private BigDecimal price;
	private TradeConstants.Status status;
	private BigDecimal commission;
	private BigDecimal stop_price;

	@Override
	public String toString() {
		String s = FormatUtil.YMD_DATE_FORMAT.format(dt);
		s += " " + side + " " + shares + " " + symbol + " @ " + FormatUtil.priceFormat(price);
		if (stop_price != null) {
			s += " STOP " + FormatUtil.priceFormat(stop_price);
		} else {
			s += " NO_STOP ";
		}
		s += " STATUS " + status;
		return s;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPosition_id() {
		return position_id;
	}

	public void setPosition_id(Long position_id) {
		this.position_id = position_id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getSettings_id() {
		return settings_id;
	}

	public void setSettings_id(Long settings_id) {
		this.settings_id = settings_id;
	}

	public Long getShares() {
		return shares;
	}

	public void setShares(Long shares) {
		this.shares = shares;
	}

	public TradeConstants.Side getSide() {
		return side;
	}

	public void setSide(TradeConstants.Side side) {
		this.side = side;
	}

	public TradeConstants.Status getStatus() {
		return status;
	}

	public void setStatus(TradeConstants.Status status) {
		this.status = status;
	}

	public BigDecimal getStop_price() {
		return stop_price;
	}

	public void setStop_price(BigDecimal stop_price) {
		this.stop_price = stop_price;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Long getTrade_system_id() {
		return trade_system_id;
	}

	public void setTrade_system_id(Long trade_system_id) {
		this.trade_system_id = trade_system_id;
	}

	/**
	 * compare by date
	 */
	public int compareTo(Object other) {
		Date date = getDt();
		Trade otherTrade = (Trade) other;
		Date otherDate = otherTrade.getDt();
		return date.compareTo(otherDate);
	}
}
