package co.kuznetsov.market.monitor;

import co.kuznetsov.market.feeds.Source;
import co.kuznetsov.market.feeds.google.GFSourceSNP;
import co.kuznetsov.market.feeds.yahoo.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class MarketMonitor {
    private SpreadHolder spreadHolder               = new SpreadHolder();
    private QuoteHolder quoteHolder                 = new QuoteHolder();
    private DeviationMonitor deviationMonitor       = new DeviationMonitor(quoteHolder);
    private AtomicReference<WarnLevel> WARN_LEVEL   = new AtomicReference<>(null);
    private SourceChain sourceChain                 = new SourceChain();

    private String[] spreadsPaths;

    public WarnLevel getCurrentWarnLevel() throws IOException {
        reloadSpreads();

        reloadQuotes();
        WarnLevel newLevel = spreadHolder.getWarnLevel(quoteHolder);
        if (WARN_LEVEL.get() != null && Math.abs(WARN_LEVEL.get().getLevel() - newLevel.getLevel()) >= 2) {
            reloadQuotes();
            newLevel = spreadHolder.getWarnLevel(quoteHolder);
        }
        WARN_LEVEL.set(newLevel);
        beginOutput(System.out);
        if (quoteHolder.isMarketOpen()) {
            quoteHolder.printQuotes(System.out, true, true);
            deviationMonitor.printDeviations(System.out, true, false);
            deviationMonitor.printDeviationsPct(System.out, true, false);
            quoteHolder.printQuotes(System.out, false, true);
            deviationMonitor.printDeviations(System.out, true, false);
            deviationMonitor.printDeviationsPct(System.out, false, false);
        } else {
            quoteHolder.printQuotes(System.out, true, true);
            quoteHolder.printQuotes(System.out, false, true);
        }
        endOutput(System.out);
        spreadHolder.printSpreads(System.out, quoteHolder);
        return newLevel;
    }

    private void beginOutput(PrintStream out) {
        String opn = String.format("%s", (quoteHolder.isMarketOpen() ? "open" : "closed"));
        out.println("----["+now()+":"+opn+"]----------------------------------------------->");
    }

    private void endOutput(PrintStream out) {
        out.println("<-------------------------------------------------------------------");
    }

    private void reloadSpreads() throws IOException  {
        spreadHolder.removeSpreads();
        SpreadsParser sp = new SpreadsParser();
        for (String path: spreadsPaths) {
            List<Spread> spreads = sp.parse(new File(path));
            for (Spread s : spreads) {
                spreadHolder.addSpread(s);
            }
        }
    }

    private void reloadQuotes() throws IOException {
        for (Ticker t: Ticker.values()) {
            loadWithRetry(t);
        }
    }

    public void setSpreadsPaths(String[] spreadsPath) {
        this.spreadsPaths = spreadsPath;
    }

    private void loadWithRetry(Ticker ticker) throws IOException {
        int retry = 5;
        do {
            try {
                retry--;
                quoteHolder.update(ticker, sourceChain.getCurrent(ticker));
                return;
            } catch(Exception e) {
                if (retry == 0) {
                    throw e;
                }
            }
        } while (true);
    }

    private String now() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(now);
    }
}
