package co.kuznetsov.market.market;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class WarnLevel {
    private int level;
    private int yesterdayLevel;
    private int delta;

    public WarnLevel(int level, int yesterdayLevel, int delta) {
        this.level = level;
        this.yesterdayLevel = yesterdayLevel;
        this.delta = delta;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getYesterdayLevel() {
        return yesterdayLevel;
    }

    public void setYesterdayLevel(int yesterdayLevel) {
        this.yesterdayLevel = yesterdayLevel;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }
}
