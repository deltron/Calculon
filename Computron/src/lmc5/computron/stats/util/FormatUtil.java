/*
 * Created on Sep 27, 2005
 */
package lmc5.computron.stats.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * @author phil
 */
public class FormatUtil {

    /**
     * date formats
     */
	public static final DateFormat YMD_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE d MMM yyyy");
    public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss");
    
    /**
     * number formats
     */
    public static final NumberFormat INTEGER_FORMAT = NumberFormat.getIntegerInstance();
    public static final NumberFormat PERCENT_FORMAT = NumberFormat.getPercentInstance();
    public static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    
    public static final NumberFormat FOUR_DECIMAL_FORMAT = new DecimalFormat("0.0000");
    
    /** no loss 8 decimal Format */
    public static final Format DEFAULT_FORMAT = new DecimalFormat("0.0#######");
    
    /** default price format for security */
    public static final Format PRICE_FORMAT = new DecimalFormat("0.0#######");
    
    
    /**
     * format method
     */
    public static String defaultFormat(Number number) {
        return formatWithCheckForNull(number, DEFAULT_FORMAT);
    }
    
    public static String integerFormat(Number number) {
        return formatWithCheckForNull(number, INTEGER_FORMAT);
    }

    public static String priceFormat(Number number) {
        return formatWithCheckForNull(number, PRICE_FORMAT);    
    }
    
    public static String fourDecimalFormat(Number number) {
        return formatWithCheckForNull(number, FOUR_DECIMAL_FORMAT);
    }
    
    public static String currencyFormat(Number number) {
        return formatWithCheckForNull(number, PRICE_FORMAT);
    }
    
    /**
     * format the number with check for null
     */
    public static String formatWithCheckForNull(Number number, Format format) {
        String formattedNumber = "null";
        if (number != null) {
            formattedNumber = format.format(number);
        }
        return formattedNumber;
    }
    
}
