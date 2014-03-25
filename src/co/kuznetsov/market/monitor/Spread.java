package co.kuznetsov.market.monitor;

import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class Spread implements Comparable<Spread> {
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

    @Override
    public int compareTo(Spread o) {
        int res = ticker.compareTo(o.ticker);
        if (res != 0) {
            return res;
        }
        if (lo.compareTo(o.lo) < 0 && hi.compareTo(o.hi) < 0) {
            return -1;
        }
        if (lo.compareTo(o.lo) > 0 && hi.compareTo(o.hi) > 0) {
            return 1;
        }
        return 1;
    }

    public String toString() {
        return ticker.name() + ":" + lo.toString() + "-" + hi.toString();
    }
}
