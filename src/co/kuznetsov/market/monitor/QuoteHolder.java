package co.kuznetsov.market.monitor;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private static final int  FIXING_THRESHOLD = 100;

    private Map<Ticker, BigDecimal> lows = new ConcurrentSkipListMap<>();
    private Map<Ticker, BigDecimal> highs = new ConcurrentSkipListMap<>();
    private Map<Ticker, BigDecimal> tickers = new ConcurrentSkipListMap<>();
    private Map<Ticker, AtomicInteger> noChangeCounters = new ConcurrentHashMap<>();
    private Fixing fixing = new Fixing();
    private MarketHash marketHash = new MarketHash(fixing);

    public boolean isMarketOpen() {
        return marketHash.getLastUpdate() > (System.currentTimeMillis() - MARKET_DATA_DOWNTIME_THRESHOLD);
    }

    public boolean isCanFixWarnLevel() {
        return marketHash.getLastUpdate() <= (System.currentTimeMillis() - MARKET_WARN_FIXING_THRESHOLD);
    }

    public void update(Ticker ticker, BigDecimal current) {
        checkHighs(ticker, current);
        checkLows(ticker, current);

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
                    highs.put(ticker, current);
                    lows.put(ticker, current);
                    fixing.fixQuote(ticker, current);
                    System.out.println("Fixing: " + ticker + ": " + current);
                }
                noChangeCounters.remove(ticker);
            }
        } else {
            noChangeCounters.remove(ticker);
        }
        marketHash.update(this);
    }

    private void checkHighs(Ticker ticker, BigDecimal current) {
        BigDecimal prevHi = this.highs.get(ticker);
        if (marketHash.getLastUpdate() > (System.currentTimeMillis() - MARKET_WARN_FIXING_THRESHOLD)) {
            if (prevHi == null) {
                prevHi = current;
            } else if (prevHi.compareTo(current) < 0) {
                prevHi = current;
            }
        } else {
            prevHi = current;
        }
        this.highs.put(ticker, prevHi);
    }

    private void checkLows(Ticker ticker, BigDecimal current) {
        BigDecimal prevLo = this.lows.get(ticker);
        if (marketHash.getLastUpdate() > (System.currentTimeMillis() - MARKET_WARN_FIXING_THRESHOLD)) {
            if (prevLo == null) {
                prevLo = current;
            } else if (prevLo.compareTo(current) < 0) {
                prevLo = current;
            }
        } else {
            prevLo = current;
        }
        this.lows.put(ticker, prevLo);
    }

    public BigDecimal getCurrent(Ticker ticker) {
        return this.tickers.get(ticker);
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
                        out.format("%4s: %7s   ", ticker.name(), tickers.get(ticker));
                    } else {
                        out.format("     %7s   ", tickers.get(ticker));
                    }
                }
            }
        } else {
            for (Ticker ticker : tickers.keySet()) {
                if (ticker.isVolatility()) {
                    if (tickerName) {
                        out.format("%4s: %7s   ", ticker.name(), tickers.get(ticker));
                    } else {
                        out.format("     %7s   ", tickers.get(ticker));
                    }
                }
            }
        }
        out.println();
    }

}
