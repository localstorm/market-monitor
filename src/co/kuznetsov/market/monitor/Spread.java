package co.kuznetsov.market.monitor;

import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class Spread implements Comparable<Spread> {
    private int id;
    private Ticker ticker;
    private BigDecimal lo;
    private BigDecimal mid;
    private BigDecimal hi;
    private Expiration expiration;
    private String source;

    public Spread(int id,
                  Ticker ticker,
                  BigDecimal lo,
                  BigDecimal mid,
                  BigDecimal hi,
                  Expiration exp,
                  String source) {
        this.id = id;
        this.ticker = ticker;
        this.lo = lo;
        this.hi = hi;
        this.expiration = exp;
        this.source = source;
        this.mid = (mid == null) ? lo.add(hi).divide(BigDecimal.valueOf(2)) : mid;
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
        int exp = expiration.compareTo(o.getExpiration());
        if (exp == 0 && equals(o)) {
            return 0;
        } else {
            exp = ticker.compareTo(o.getTicker());
            if (exp == 0) {
                return (id - o.id);
            } else {
                return exp;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Spread spread = (Spread) o;

        if (!expiration.equals(spread.expiration)) return false;
        if (!hi.equals(spread.hi)) return false;
        if (!lo.equals(spread.lo)) return false;
        if (!mid.equals(spread.mid)) return false;
        if (ticker != spread.ticker) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ticker.hashCode();
        result = 31 * result + lo.hashCode();
        result = 31 * result + mid.hashCode();
        result = 31 * result + hi.hashCode();
        result = 31 * result + expiration.hashCode();
        return result;
    }

    public String toString() {
        String expFormat    = "Exp: %8s, %3s days left";
        String spreadFormat = "%s: %s-(%s)-%s\t%s (%s)";
        String exp = (expiration != null) ?
                      String.format(expFormat, expiration, ""+expiration.daysLeft()) :
                      String.format(expFormat, "?", "?");
        return String.format(spreadFormat, ticker, lo, mid, hi, exp, source);
    }
}
