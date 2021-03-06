package co.kuznetsov.market.monitor;

import java.math.BigDecimal;

public class Deviation {
    private Ticker      ticker;
    private BigDecimal  currentDeviation;
    private BigDecimal  currentDeviationPct;

    public Deviation(Ticker ticker, BigDecimal current, BigDecimal fixing) {
        this.ticker  = ticker;
        this.currentDeviation    = current.subtract(fixing);
        if (fixing.equals(BigDecimal.ZERO)) {
            this.currentDeviationPct = BigDecimal.ZERO;
        } else {
            double d1 = currentDeviation.doubleValue();
            double d2 = fixing.doubleValue();
            this.currentDeviationPct = new BigDecimal((int)(d1/d2 * 10000)).divide(new BigDecimal(100));
        }
    }

    public Ticker getTicker() {
        return ticker;
    }

    public BigDecimal getCurrentDeviation() {
        return currentDeviation;
    }

    public BigDecimal getCurrentDeviationPct() {
        return currentDeviationPct;
    }

}
