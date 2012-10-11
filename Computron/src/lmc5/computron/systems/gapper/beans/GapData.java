package lmc5.computron.systems.gapper.beans;

import java.math.BigDecimal;
import java.util.Date;

public class GapData {
	private String symbol;
	private String exchange;
	private Date dt;
	private BigDecimal prev_close;
	private BigDecimal drift;
	private BigDecimal open;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public BigDecimal getPrev_close() {
		return prev_close;
	}

	public void setPrev_close(BigDecimal prev_close) {
		this.prev_close = prev_close;
	}

	public BigDecimal getDrift() {
		return drift;
	}

	public void setDrift(BigDecimal drift) {
		this.drift = drift;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

}
