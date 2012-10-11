/*
 * Created on May 15, 2008
 */
package lmc5.computron.stats.functions.positions.aggregate.average;

import com.google.common.base.Function;
import java.math.BigDecimal;
import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.functions.positions.aggregate.count.PercentWin;
import lmc5.computron.stats.util.MathUtil;

/**
 * There are two basic components to the Kelly Criterion:
 * 
 * 1) Win probability - The probability that any given trade you make will
 * return a positive amount.
 * 
 * 2) Win/loss ratio - The total positive trade amounts divided by the total
 * negative trade amounts.
 * 
 * These two factors are then put into Kelly's equation:
 * 
 * Kelly % = W – [(1 – W) / R]
 * 
 * Where:
 * 
 * <li>W = Winning probability
 * <li>R = Win/loss ratio
 * 
 * The output is the Kelly percentage.
 * 
 * The percentage (a number less than one) that the equation produces represents
 * the size of the positions you should be taking. For example, if the Kelly
 * percentage is 0.05, then you should take a 5% position in each of the
 * equities in your portfolio.
 * 
 * http://www.investopedia.com/articles/trading/04/091504.asp
 */
public class KellyCriterion implements Function<Iterable<Position>, BigDecimal> {
	public BigDecimal apply(Iterable<Position> positions) {
		BigDecimal winProbability = new PercentWin().apply(positions);
		BigDecimal averageLoss = new AverageLossValue().apply(positions);

		if (averageLoss.signum() == 0) {
			// never lose!
			return BigDecimal.ONE;
		} else {
			BigDecimal r = new AverageWinValue().apply(positions).divide(averageLoss, MathUtil.MATH_CONTEXT);

			if (r.signum() == 0) {
				// Never win, bet nothing
				return BigDecimal.ZERO;
			} else {
				BigDecimal oneMinusWoverR = BigDecimal.ONE.subtract(winProbability).divide(r, MathUtil.MATH_CONTEXT);
				return winProbability.subtract(oneMinusWoverR);
			}
		}
	}
}
