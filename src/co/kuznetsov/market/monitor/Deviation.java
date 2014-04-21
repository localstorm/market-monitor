package co.kuznetsov.market.monitor;

import java.math.BigDecimal;

/**
 * Created by localstorm on 4/5/14.
 */
public class Deviation implements Comparable<Deviation> {
    private Ticker ticker;
    private BigDecimal current;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal deviation;
    private boolean opportunity;

    public Deviation(Ticker ticker,
                     BigDecimal current,
                     BigDecimal low,
                     BigDecimal high) {
        this.ticker  = ticker;
        this.current = current;
        this.low = low;
        this.high = high;
        if (current.subtract(low).compareTo(high.subtract(current)) >= 0) {
            deviation = current.subtract(low);
        } else {
            deviation = current.subtract(high);
        }
        this.opportunity = false;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public BigDecimal getCurrent() {
        return current;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public boolean isOpportunity() {
        return opportunity;
    }

    public void setOpportunity(boolean opportunity) {
        this.opportunity = opportunity;
    }

    public BigDecimal getDeviation() {
        return this.deviation;
    }

    @Override
    public int compareTo(Deviation o) {
        return Double.compare(Math.abs(deviation.doubleValue()), Math.abs(o.deviation.doubleValue()));
    }

    @Override
    public String toString() {
        return "deviation(" + ticker.name() + "): " + deviation;
    }


}
