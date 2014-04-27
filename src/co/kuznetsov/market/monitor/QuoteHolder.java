package co.kuznetsov.market.monitor;

import co.kuznetsov.market.feeds.HiLo;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class QuoteHolder {
    private static final long MARKET_WARN_FIXING_THRESHOLD   = 1200000L; // 20 minutes of downtime -> fix
    private static final long MARKET_DATA_DOWNTIME_THRESHOLD = 120000L;  // 2 minutes of downtime
    private static final int  FIXING_THRESHOLD               = 100;

    private Map<Ticker, HiLo>                  range52w = new ConcurrentSkipListMap<>();
    private Map<Ticker, BigDecimal>             tickers = new ConcurrentSkipListMap<>();
    private Map<Ticker, AtomicInteger> noChangeCounters = new ConcurrentHashMap<>();
    private Fixing                               fixing = new Fixing();
    private MarketHash                       marketHash = new MarketHash(fixing);

    public boolean isMarketOpen() {
        return marketHash.getLastUpdate() > (System.currentTimeMillis() - MARKET_DATA_DOWNTIME_THRESHOLD);
    }

    public boolean isCanFixWarnLevel() {
        return marketHash.getLastUpdate() <= (System.currentTimeMillis() - MARKET_WARN_FIXING_THRESHOLD);
    }

    public void update(Ticker ticker, BigDecimal current) {
        BigDecimal prev   = this.tickers.put(ticker, current);
        if (prev != null && prev.equals(current)) {
            AtomicInteger count = noChangeCounters.get(ticker);
            if (count == null) {
                count = new AtomicInteger(1);
                noChangeCounters.put(ticker, count);
            } else {
                count.incrementAndGet();
            }
            if (count.get() >= FIXING_THRESHOLD) {
                if (!current.equals(fixing.getQuote(ticker))) {
                    fixing.fixQuote(ticker, current);
                }
                noChangeCounters.remove(ticker);
            }
        } else {
            noChangeCounters.remove(ticker);
        }
        marketHash.update(this);
    }

    public void update52wRange(Ticker ticker, HiLo range) {
        range52w.put(ticker, range);
    }

    public BigDecimal getCurrent(Ticker ticker) {
        return this.tickers.get(ticker);
    }

    public Integer getRank(Ticker ticker) {
        HiLo range = this.range52w.get(ticker);
        if (range == null) {
            return null;
        }
        BigDecimal rank = getCurrent(ticker).subtract(range.getLow());
        rank = rank.divide(range.getHigh().subtract(range.getLow()), new MathContext(2));
        return rank.multiply(new BigDecimal(100)).intValue();
    }

    public BigDecimal getLastFixing(Ticker ticker) {
        BigDecimal f = this.fixing.getQuote(ticker);
        if (f == null) {
            f = this.getCurrent(ticker);
        }
        return f;
    }

    public int getLastWarningLevel() {
        Integer wl = this.fixing.getWarnLevel();
        return (wl == null) ? -1 : wl;
    }

    public void fixWarnLevel(int currentLevel) {
        fixing.fixWarnLevel(currentLevel);
    }

    public void printQuotes(PrintStream out, boolean index, boolean tickerName) {
        if (index) {
            for (Ticker ticker : tickers.keySet()) {
                if (ticker.isIndex()) {
                    if (tickerName) {
                        out.format("%5s: %7s   ", ticker.name(), tickers.get(ticker));
                    } else {
                        out.format("      %7s   ", tickers.get(ticker));
                    }
                }
            }
        } else {
            for (Ticker ticker : tickers.keySet()) {
                if (ticker.isVolatility()) {
                    if (tickerName) {
                        out.format("%5s: %7s   ", ticker.name(), tickers.get(ticker));
                    } else {
                        out.format("      %7s   ", tickers.get(ticker));
                    }
                }
            }
        }
        out.println();
    }

}
