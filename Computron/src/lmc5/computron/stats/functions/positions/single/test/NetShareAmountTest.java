package lmc5.computron.stats.functions.positions.single.test;

import java.util.HashSet;
import java.util.Set;

import lmc5.computron.common.beans.Trade;
import lmc5.computron.common.constants.TradeConstants.Side;

import org.junit.Test;

public class NetShareAmountTest {
	@Test
	public void testNetShareAmout() {
		Trade t1 = new Trade();
		t1.setSide(Side.BUY);
		t1.setShares(1000l);

		Trade t2 = new Trade();
		t2.setSide(Side.SELL);
		t2.setShares(1000l);

		Set<Trade> trades = new HashSet<Trade>();
		trades.add(t1);
		trades.add(t2);

		//BigDecimal netShares = new NetShareAmount().apply(trades);

		//Assert.assertEquals("Net shares is 0", new BigDecimal("0"), netShares);
	}

	@Test
	public void testNetShareAmoutPositive() {
		Trade t1 = new Trade();
		t1.setSide(Side.BUY);
		t1.setShares(1000l);

		Trade t2 = new Trade();
		t2.setSide(Side.SELL);
		t2.setShares(500l);

		Set<Trade> trades = new HashSet<Trade>();
		trades.add(t1);
		trades.add(t2);

	//	BigDecimal netShares = new NetShareAmount().apply(trades);

	//	Assert.assertEquals("Net shares is +500", new BigDecimal("500"), netShares);
	}

	@Test
	public void testNetShareAmoutNegative() {
		Trade t1 = new Trade();
		t1.setSide(Side.BUY);
		t1.setShares(500l);

		Trade t2 = new Trade();
		t2.setSide(Side.SELL);
		t2.setShares(1000l);

		Set<Trade> trades = new HashSet<Trade>();
		trades.add(t1);
		trades.add(t2);

	//	BigDecimal netShares = new NetShareAmount().apply(trades);

	//	Assert.assertEquals("Net shares is -500", new BigDecimal("-500"), netShares);
	}
	
	@Test
	public void testNetShareAmoutEmptyTrades() {
		Set<Trade> trades = new HashSet<Trade>();

	//	BigDecimal netShares = new NetShareAmount().apply(trades);

	//	Assert.assertEquals("Net shares is 0 (empty set)", new BigDecimal("0"), netShares);
	}
}
