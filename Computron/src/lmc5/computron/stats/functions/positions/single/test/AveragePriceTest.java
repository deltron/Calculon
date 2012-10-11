package lmc5.computron.stats.functions.positions.single.test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import lmc5.computron.common.beans.Trade;

import org.junit.Test;

public class AveragePriceTest {
	@Test
	public void testAveragePrice() {
		Trade t1 = new Trade();
		t1.setShares(1000l);
		t1.setPrice(new BigDecimal("10"));

		Trade t2 = new Trade();
		t2.setShares(1000l);
		t2.setPrice(new BigDecimal("20"));

		Set<Trade> trades = new HashSet<Trade>();
		trades.add(t1);
		trades.add(t2);

	//	BigDecimal averagePrice = new AverageEntryPrice().apply(trades);

	//	Assert.assertEquals("Average Price is 15", new BigDecimal("15"), averagePrice);
	}
	
	@Test
	public void testAveragePriceEmptyTrades() {
		Set<Trade> trades = new HashSet<Trade>();
		
	//	BigDecimal averagePrice = new AverageEntryPrice().apply(trades);

		// should get some kind of exception
	}
}
