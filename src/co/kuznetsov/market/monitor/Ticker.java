package co.kuznetsov.market.monitor;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public enum Ticker {
    SNP(true, false, 0),
    RUT(true, false, -1),
    NDX(true, false, -1),
    VIX(false, true, 0),
    RVX(false, true, 0),
    QQV(false, true, 0);

    private final boolean volatility;
    private final boolean index;
    private final int     expirationOffset;

    Ticker(boolean index, boolean volatility, int expirationOffset) {
        this.index = index;
        this.volatility = volatility;
        this.expirationOffset = expirationOffset;
    }

    public boolean isVolatility() {
        return volatility;
    }

    public boolean isIndex() {
        return index;
    }

    public int getExpirationOffset() {
        return expirationOffset;
    }
}
