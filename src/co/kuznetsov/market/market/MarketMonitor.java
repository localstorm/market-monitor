package co.kuznetsov.market.market;

import co.kuznetsov.market.feeds.*;

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


    public WarnLevel evalDangerLevel() throws IOException {
        reloadSpreads();
        reloadQuotes();
        WarnLevel wl = spreadHolder.getWarnLevel(quoteHolder);
        System.out.println("Warn Level: " + wl.getLevel());
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
        quoteHolder.update(snp500.getTicker(), snp500.getCurrent());
        quoteHolder.update(rut.getTicker(), rut.getCurrent());
        quoteHolder.update(ndx.getTicker(), ndx.getCurrent());
        quoteHolder.update(vix.getTicker(), vix.getCurrent());
        quoteHolder.printQuotes(System.out);
    }

    public void setSpreadsPath(String spreadsPath) {
        this.spreadsPath = spreadsPath;
    }

}
