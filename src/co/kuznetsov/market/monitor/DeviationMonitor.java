package co.kuznetsov.market.monitor;

import java.io.PrintStream;
import java.math.BigDecimal;

import static co.kuznetsov.market.util.DecimalUtil.signed;

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
                        out.format("%4s: %6s%%   ", ticker.name(), signed(d.getCurrentDeviationPct()));
                    } else {
                        out.format("       %6s%%  ", signed(d.getCurrentDeviationPct()));
                    }
                }
            }
        } else {
            for (Ticker ticker : Ticker.values()) {
                if (ticker.isVolatility()) {
                    Deviation d = getDeviation(ticker);
                    if (tickerName) {
                        out.format("%4s: %6s%%   ", ticker.name(), signed(d.getCurrentDeviationPct()));
                    } else {
                        out.format("       %6s%%  ", signed(d.getCurrentDeviationPct()));
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
                        out.format("%4s: %6s   ", ticker.name(), signed(d.getCurrentDeviation()));
                    } else {
                        out.format("       %6s   ", signed(d.getCurrentDeviation()));
                    }
                }
            }
        } else {
            for (Ticker ticker : Ticker.values()) {
                if (ticker.isVolatility()) {
                    Deviation d = getDeviation(ticker);
                    if (tickerName) {
                        out.format("%4s: %6s   ", ticker.name(), signed(d.getCurrentDeviation()));
                    } else {
                        out.format("       %6s   ", signed(d.getCurrentDeviation()));
                    }
                }
            }
        }
        out.println();
    }
}
