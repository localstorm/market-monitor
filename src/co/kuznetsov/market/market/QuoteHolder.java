package co.kuznetsov.market.market;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class QuoteHolder {
    private Map<Ticker, BigDecimal> tickers = new ConcurrentSkipListMap<Ticker, BigDecimal>();

    public void update(Ticker ticker, BigDecimal current) {
        this.tickers.put(ticker, current);
    }

    public BigDecimal getCurrent(Ticker ticker) {
        return this.tickers.get(ticker);
    }

    public void printQuotes(PrintStream out) {
        for (Ticker ticker: tickers.keySet()) {
            out.print(ticker.name() + ": " + tickers.get(ticker));
            out.print("   ");
        }
        out.println();
    }
}
