package co.kuznetsov.market.monitor;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class WarnLevel {
    private int level;
    private int delta;
    private boolean marketOpen;

    public WarnLevel(int level, int delta, boolean marketOpen) {
        this.level = level;
        this.delta = delta;
        this.marketOpen = marketOpen;
    }

    public int getLevel() {
        return level;
    }

    public int getDelta() {
        return delta;
    }

    public boolean isMarketOpen() {
        return marketOpen;
    }

}
