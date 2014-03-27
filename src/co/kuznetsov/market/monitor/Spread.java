package co.kuznetsov.market.monitor;

import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class Spread implements Comparable<Spread> {
    private Ticker ticker;
    private BigDecimal lo;
    private BigDecimal mid;
    private BigDecimal hi;
    private Expiration expiration;

    public Spread(Ticker ticker, BigDecimal lo, BigDecimal mid, BigDecimal hi, Expiration exp) {
        this.ticker = ticker;
        this.lo = lo;
        this.hi = hi;
        this.mid = (mid == null) ? lo.add(hi).divide(BigDecimal.valueOf(2)) : mid;
        this.expiration = exp;
    }

    public BigDecimal getLo() {
        return lo;
    }

    public BigDecimal getHi() {
        return hi;
    }

    public BigDecimal getMid() {
        return mid;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public Expiration getExpiration() {
        return expiration;
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
        String exp = (expiration != null) ?
                      String.format("Exp: %8s, %3s days left", expiration, ""+expiration.daysLeft()) :
                      String.format("Exp: %8s, %3s days left", "?", "?");
        return String.format("%s: %s-(%s)-%s\t%s", ticker, lo, mid, hi, exp);
    }
}
