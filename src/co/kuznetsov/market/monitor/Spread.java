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
        return expiration.compareTo(o.getExpiration());
    }

    public String toString() {
        String expFormat    = "Exp: %8s, %3s days left";
        String spreadFormat = "%s: %s-(%s)-%s\t%s";
        String exp = (expiration != null) ?
                      String.format(expFormat, expiration, ""+expiration.daysLeft()) :
                      String.format(expFormat, "?", "?");
        return String.format(spreadFormat, ticker, lo, mid, hi, exp);
    }
}
