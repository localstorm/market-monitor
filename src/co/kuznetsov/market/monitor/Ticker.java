package co.kuznetsov.market.monitor;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public enum Ticker {
    SNP(true, false),
    RUT(true, false),
    NDQ(true, false),
    VIX(false, true),
    RVX(false, true),
    QQV(false, true);

    private final boolean volatility;
    private final boolean index;

    Ticker(boolean index, boolean volatility) {
        this.index = index;
        this.volatility = volatility;
    }

    public boolean isVolatility() {
        return volatility;
    }

    public boolean isIndex() {
        return index;
    }
}
