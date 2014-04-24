package co.kuznetsov.market.monitor;

import java.io.PrintStream;
import java.math.BigDecimal;

public class DeviationMonitor {
    private QuoteHolder quoteHolder;

    public DeviationMonitor(QuoteHolder quoteHolder) {
        this.quoteHolder = quoteHolder;
    }

    public Deviation getDeviation(Ticker t) {
        BigDecimal fix = this.quoteHolder.getLastFixing(t);
        BigDecimal cur = this.quoteHolder.getCurrent(t);
        return new Deviation(t, cur, fix);
    }

    public void printDeviationsPct(PrintStream out, boolean index, boolean tickerName) {
        if (index) {
            for (Ticker ticker : Ticker.values()) {
                if (ticker.isIndex()) {
                    Deviation d = getDeviation(ticker);
                    if (tickerName) {
                        out.format("%4s: %6s%%   ", ticker.name(), d.getCurrentDeviationPct());
                    } else {
                        out.format("       %6s%%  ", d.getCurrentDeviationPct());
                    }
                }
            }
        } else {
            for (Ticker ticker : Ticker.values()) {
                if (ticker.isVolatility()) {
                    Deviation d = getDeviation(ticker);
                    if (tickerName) {
                        out.format("%4s: %6s%%   ", ticker.name(), d.getCurrentDeviationPct());
                    } else {
                        out.format("       %6s%%  ", d.getCurrentDeviationPct());
                    }
                }
            }
        }
        out.println();
    }

    public void printDeviations(PrintStream out, boolean index, boolean tickerName) {
        if (index) {
            for (Ticker ticker : Ticker.values()) {
                if (ticker.isIndex()) {
                    Deviation d = getDeviation(ticker);
                    if (tickerName) {
                        out.format("%4s: %6s   ", ticker.name(), d.getCurrentDeviation());
                    } else {
                        out.format("       %6s   ", d.getCurrentDeviation());
                    }
                }
            }
        } else {
            for (Ticker ticker : Ticker.values()) {
                if (ticker.isVolatility()) {
                    Deviation d = getDeviation(ticker);
                    if (tickerName) {
                        out.format("%4s: %6s   ", ticker.name(), d.getCurrentDeviation());
                    } else {
                        out.format("       %6s   ", d.getCurrentDeviation());
                    }
                }
            }
        }
        out.println();
    }
}
