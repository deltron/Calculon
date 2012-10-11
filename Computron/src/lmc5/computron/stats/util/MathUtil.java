/*
 * Created on Jan 10, 2005
 */
package lmc5.computron.stats.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * MathUtil
 * 
 * @author phil
 */
public class MathUtil {

   public static final BigDecimal ZERO = new BigDecimal("0.00000000");
   
   public static final BigDecimal ZERO_INTEGER = new BigDecimal("0");
   
   public static final BigDecimal ONE = new BigDecimal("1.00000000");
   
   public static final BigDecimal ONE_INTEGER = new BigDecimal("1");
   
   public static final BigDecimal TWO = new BigDecimal("2.00000000");
   
   public static final BigDecimal THREE = new BigDecimal("3.00000000");

   public static final int SCALE = 8;
   
   public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
   
   public static final MathContext MATH_CONTEXT = new MathContext(SCALE, ROUNDING_MODE);
}
