package co.kuznetsov.market.market;

import co.kuznetsov.market.feeds.Source;
import co.kuznetsov.market.feeds.SourceNDQ;
import co.kuznetsov.market.feeds.SourceRUT;
import co.kuznetsov.market.feeds.SourceSNP500;

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

    private String fixingTime = "00:00";
    private String spreadsPath;

    private Source snp500 = new SourceSNP500();
    private Source rut = new SourceRUT();
    private Source ndx = new SourceNDQ();


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
        quoteHolder.printQuotes(System.out);
    }

    public void setSpreadsPath(String spreadsPath) {
        this.spreadsPath = spreadsPath;
    }

    public void setFixingTime(String fixingTime) {
        this.fixingTime = fixingTime;
    }
}
