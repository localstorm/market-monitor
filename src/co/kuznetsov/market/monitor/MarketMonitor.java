package co.kuznetsov.market.monitor;

import co.kuznetsov.market.feeds.Source;
import co.kuznetsov.market.feeds.yahoo.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class MarketMonitor {
    private SpreadHolder spreadHolder = new SpreadHolder();
    private QuoteHolder quoteHolder   = new QuoteHolder();
    private TickerDeviations tickerDeviations = new TickerDeviations();
    private DeviationMonitor deviationMonitor = new DeviationMonitor(quoteHolder, tickerDeviations);

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
        WarnLevel before = spreadHolder.getWarnLevel(quoteHolder);
        reloadSpreads();
        reloadQuotes();
        WarnLevel after = spreadHolder.getWarnLevel(quoteHolder);
        if (Math.abs(before.getLevel()-after.getLevel())>=2) {
            reloadSpreads();
            reloadQuotes();
            return spreadHolder.getWarnLevel(quoteHolder);
        }
        return after;
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
        quoteHolder.printQuotes(System.out);
        //deviationMonitor.printOpportunities();
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
