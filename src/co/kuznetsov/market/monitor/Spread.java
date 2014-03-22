package co.kuznetsov.market.monitor;

import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class Spread {
    private Ticker ticker;
    private BigDecimal lo;
    private BigDecimal hi;

    public Spread(Ticker ticker, BigDecimal lo, BigDecimal hi) {
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

    public Ticker getTicker() {
        return ticker;
    }
}
