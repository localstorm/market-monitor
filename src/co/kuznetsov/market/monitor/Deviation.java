package co.kuznetsov.market.monitor;

import java.math.BigDecimal;
import java.math.MathContext;

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
            this.currentDeviationPct = new BigDecimal(((int)(currentDeviation.doubleValue() / fixing.doubleValue() * 10000)) / 100.0, new MathContext(2));
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
