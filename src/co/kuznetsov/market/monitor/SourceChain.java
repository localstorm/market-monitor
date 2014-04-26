package co.kuznetsov.market.monitor;

import co.kuznetsov.market.feeds.Source;
import co.kuznetsov.market.feeds.google.*;
import co.kuznetsov.market.feeds.yahoo.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author localstorm
 *         Date: 4/24/14
 */
public class SourceChain {

    private Map<Ticker, Source> primary = new HashMap<Ticker, Source>() {{
        put(Ticker.SNP, new YFSourceSNP());
        put(Ticker.NDQ, new YFSourceNDQ());
        put(Ticker.RUT, new YFSourceRUT());
        put(Ticker.OEX, new YFSourceOEX());
        put(Ticker.RVX, new YFSourceRVX());
        put(Ticker.VIX, new GFSourceVIX());
        put(Ticker.QQV, new YFSourceQQV());
        put(Ticker.VXO, new YFSourceVXO());
    }};

    private Map<Ticker, Source> backup = new HashMap<Ticker, Source>() {{
        put(Ticker.SNP, new GFSourceSNP());
        put(Ticker.NDQ, new GFSourceNDQ());
        put(Ticker.RUT, new GFSourceRUT());
        put(Ticker.OEX, new GFSourceOEX());
        put(Ticker.RVX, new GFSourceRVX());
        put(Ticker.VIX, new YFSourceVIX());
        put(Ticker.QQV, new GFSourceQQV());
        put(Ticker.VXO, new GFSourceVXO());
    }};

    public BigDecimal getCurrent(Ticker ticker) throws IOException {
        try {
            return primary.get(ticker).getCurrent();
        } catch (IOException e) {
            return backup.get(ticker).getCurrent();
        }
    }
}
