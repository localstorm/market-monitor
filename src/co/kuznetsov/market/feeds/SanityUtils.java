package co.kuznetsov.market.feeds;

import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class SanityUtils {

    public static BigDecimal sanity(String name, BigDecimal value, int min, int max) {
        if (value.compareTo(new BigDecimal(max)) >= 0) {
            throw new IllegalArgumentException("Too big value: " + name + "=" + value);
        }
        if (value.compareTo(new BigDecimal(min)) <= 0) {
            throw new IllegalArgumentException("Too small value: " + name + "=" + value);
        }
        return value;
    }
}
