package co.kuznetsov.market.market;

import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class Spread {
    private String ticker;
    private BigDecimal lo;
    private BigDecimal hi;

    public Spread(String ticker, BigDecimal lo, BigDecimal hi) {
        this.ticker = ticker;
        this.lo = lo;
        this.hi = hi;
    }

    public BigDecimal getLo() {
        return lo;
    }

    public BigDecimal getHi() {
        return hi;
    }

    public String getTicker() {
        return ticker;
    }
}
