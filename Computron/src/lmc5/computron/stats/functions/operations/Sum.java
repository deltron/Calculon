package lmc5.computron.stats.functions.operations;

import java.math.BigDecimal;

import com.google.common.base.Function;

public class Sum implements Function<Iterable<BigDecimal>, BigDecimal> {

	public BigDecimal apply(Iterable<BigDecimal> numbers) {
		BigDecimal total = BigDecimal.ZERO;
		
		for (BigDecimal number : numbers)
			total = total.add(number);
		
		return total;
	}

}
