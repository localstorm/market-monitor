package co.kuznetsov.market.market;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class QuoteHolder {
    private ConcurrentHashMap<String, BigDecimal> tickers = new ConcurrentHashMap<String, BigDecimal>();

    public void update(String ticker, BigDecimal current) {
        this.tickers.put(ticker, current);
    }

    public BigDecimal getCurrent(String ticker) {
        return this.tickers.get(ticker);
    }

    public void printQuotes(PrintStream out) {
        for (String ticker: tickers.keySet()) {
            out.println(ticker+": "+tickers.get(ticker));
        }
    }
}
