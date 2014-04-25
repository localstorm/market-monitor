package co.kuznetsov.market.util;

import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 4/24/14
 */
public class DecimalUtil {
    public static String signed(BigDecimal bd) {
        int cmp = bd.compareTo(BigDecimal.ZERO);
        if (cmp > 0) {
            return "+" + bd.toString();
        }
        return bd.toString();
    }
}
