package co.kuznetsov.market.monitor;

import co.kuznetsov.market.feeds.*;
import co.kuznetsov.market.feeds.yahoo.SourceNDQ;
import co.kuznetsov.market.feeds.yahoo.SourceRUT;
import co.kuznetsov.market.feeds.yahoo.SourceSNP500;
import co.kuznetsov.market.feeds.yahoo.SourceVIX;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class MarketMonitor {
    private SpreadHolder spreadHolder = new SpreadHolder();
    private QuoteHolder quoteHolder = new QuoteHolder();

    private String spreadsPath;

    private Source snp500 = new SourceSNP500();
    private Source rut = new SourceRUT();
    private Source ndx = new SourceNDQ();
    private Source vix = new SourceVIX();


    public WarnLevel getCurrentWarnLevel() throws IOException {
        reloadSpreads();
        reloadQuotes();
        WarnLevel wl = spreadHolder.getWarnLevel(quoteHolder);
        return wl;
    }

    private void reloadSpreads() throws IOException  {
        spreadHolder.removeSpreads();
        SpreadsParser sp = new SpreadsParser();
        List<Spread> spreads = sp.parse(new File(spreadsPath));
        for (Spread s: spreads) {
            spreadHolder.addSpread(s);
        }
    }

    private void reloadQuotes() throws IOException {
        loadWithRetry(snp500);
        loadWithRetry(rut);
        loadWithRetry(ndx);
        loadWithRetry(vix);
        quoteHolder.printQuotes(System.out);
    }

    public void setSpreadsPath(String spreadsPath) {
        this.spreadsPath = spreadsPath;
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
