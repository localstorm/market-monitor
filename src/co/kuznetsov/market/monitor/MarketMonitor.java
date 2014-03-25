package co.kuznetsov.market.monitor;

import co.kuznetsov.market.feeds.*;
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
    private QuoteHolder quoteHolder = new QuoteHolder();

    private String spreadsPath;

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
        return spreadHolder.getWarnLevel(quoteHolder);
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
        for (Source s: sources) {
            loadWithRetry(s);
        }
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
