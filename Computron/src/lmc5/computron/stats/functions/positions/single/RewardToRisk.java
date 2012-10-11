package lmc5.computron.stats.functions.positions.single;

import java.math.BigDecimal;

import lmc5.computron.stats.domain.Position;
import lmc5.computron.stats.util.MathUtil;

import com.google.common.base.Function;

public class RewardToRisk implements Function<Position, BigDecimal> {

	public BigDecimal apply(Position position) {
		BigDecimal initialRisk = new EntryRisk().apply(position);
		return new ProfitAndLoss().apply(position).divide(initialRisk, MathUtil.MATH_CONTEXT);
	}

}
