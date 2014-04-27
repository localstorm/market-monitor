package co.kuznetsov.market.feeds;

import java.math.BigDecimal;


public class HiLo {
    private final BigDecimal high;
    private final BigDecimal low;

    public HiLo(BigDecimal hi, BigDecimal lo) {
        this.high = hi;
        this.low  = lo;
    }

    public BigDecimal getHigh() {
        return this.high;
    }

    public BigDecimal getLow() {
        return this.low;
    }
}
