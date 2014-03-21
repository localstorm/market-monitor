package co.kuznetsov.market.market;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class WarnLevel {
    private int level;
    private int delta;

    public WarnLevel(int level, int delta) {
        this.level = level;
        this.delta = delta;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }
}
