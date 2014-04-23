package co.kuznetsov.market.monitor;

import co.kuznetsov.market.feeds.Source;
import co.kuznetsov.market.feeds.yahoo.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class MarketMonitor {
    private SpreadHolder spreadHolder           = new SpreadHolder();
    private QuoteHolder quoteHolder             = new QuoteHolder();
    private TickerDeviations tickerDeviations   = new TickerDeviations();
    private DeviationMonitor deviationMonitor   = new DeviationMonitor(quoteHolder, tickerDeviations);
    private AtomicReference<WarnLevel> WARN_LEVEL = new AtomicReference<>(null);

    private String[] spreadsPaths;

    private Source[] sources = new Source[]{
            new SourceSNP(),
            new SourceRUT(),
            new SourceNDQ(),
            new SourceVIX(),
            new SourceRVX(),
            new SourceQQV(),
            new SourceOEX(),
            new SourceVXO()
    };

    public WarnLevel getCurrentWarnLevel() throws IOException {
        reloadSpreads();

        reloadQuotes();
        WarnLevel newLevel = spreadHolder.getWarnLevel(quoteHolder);
        if (WARN_LEVEL.get() != null && Math.abs(WARN_LEVEL.get().getLevel() - newLevel.getLevel()) >= 2) {
            reloadQuotes();
            newLevel = spreadHolder.getWarnLevel(quoteHolder);
        }
        WARN_LEVEL.set(newLevel);
        quoteHolder.printQuotes(System.out);
        spreadHolder.printSpreads(quoteHolder);

        //deviationMonitor.printOpportunities();
        return newLevel;
    }

    public Deviation getHighestDeviation() {
        return this.deviationMonitor.getHighestDeviation();
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
        for (Source s: sources) {
            loadWithRetry(s);
        }
    }

    public void setSpreadsPaths(String[] spreadsPath) {
        this.spreadsPaths = spreadsPath;
    }

    private void loadWithRetry(Source src) throws IOException {
        int retry = 5;
        do {
            try {
                retry--;
                quoteHolder.update(src.getTicker(), src.getCurrent());
                return;
            } catch(Exception e) {
                if (retry == 0) {
                    throw e;
                }
            }
        } while (true);
    }
}
