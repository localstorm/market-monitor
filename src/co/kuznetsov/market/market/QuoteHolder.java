package co.kuznetsov.market.market;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class QuoteHolder {
    private Map<Ticker, BigDecimal> tickers = new ConcurrentSkipListMap<Ticker, BigDecimal>();
    private Map<Ticker, AtomicInteger> noChangeCounters = new ConcurrentHashMap<Ticker, AtomicInteger>();
    private Fixing fixing = new Fixing();

    public void update(Ticker ticker, BigDecimal current) {
        BigDecimal prev = this.tickers.put(ticker, current);
        if (prev != null && prev.equals(current)) {
            AtomicInteger count = noChangeCounters.get(ticker);
            if (count == null) {
                count = new AtomicInteger(1);
                noChangeCounters.put(ticker, count);
            } else {
                count.incrementAndGet();
            }
            if (count.get() >= 100) {
                fixing.fixQuote(ticker, current);
                noChangeCounters.remove(ticker);
            }
        } else {
            noChangeCounters.remove(ticker);
        }
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

    public void printQuotes(PrintStream out) {
        for (Ticker ticker : tickers.keySet()) {
            out.print(ticker.name() + ": " + tickers.get(ticker));
            out.print("   ");
        }
        out.println();
    }
}
