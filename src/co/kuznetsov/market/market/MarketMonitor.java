package co.kuznetsov.market.market;

import co.kuznetsov.market.sources.Source;
import co.kuznetsov.market.sources.SourceNDX;
import co.kuznetsov.market.sources.SourceRUT;
import co.kuznetsov.market.sources.SourceSNP500;

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

    private String configPath;

    private Source snp500 = new SourceSNP500();
    private Source rut = new SourceRUT();
    private Source ndx = new SourceNDX();

    public int evalDangerLevel() throws IOException {
        reloadSpreads();
        reloadQuotes();
        return spreadHolder.getWarnLevel(quoteHolder);
    }

    private void reloadSpreads() throws IOException  {
        spreadHolder.removeSpreads();
        SpreadsParser sp = new SpreadsParser();
        List<Spread> spreads = sp.parse(new File(configPath));
        for (Spread s: spreads) {
            spreadHolder.addSpread(s);
        }
    }

    private void reloadQuotes() throws IOException {
        quoteHolder.update("SNP500", snp500.getCurrent());
        quoteHolder.update("RUT", rut.getCurrent());
        quoteHolder.update("NDX", ndx.getCurrent());
        quoteHolder.printQuotes(System.out);
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }
}
