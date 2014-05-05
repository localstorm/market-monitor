package co.kuznetsov.market.monitor;

import co.kuznetsov.market.feeds.HiLo;
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
        put(Ticker.SNP, new GFSourceSNP());
        put(Ticker.NDX, new GFSourceNDX());
        put(Ticker.RUT, new GFSourceRUT());
        put(Ticker.RVX, new GFSourceRVX());
        put(Ticker.VIX, new GFSourceVIX());
        put(Ticker.QQV, new GFSourceQQV());
    }};

    private Map<Ticker, Source> backup = new HashMap<Ticker, Source>() {{
        put(Ticker.SNP, new YFSourceSNP());
        put(Ticker.NDX, new YFSourceNDX());
        put(Ticker.RUT, new YFSourceRUT());
        put(Ticker.RVX, new YFSourceRVX());
        put(Ticker.VIX, new YFSourceVIX());
        put(Ticker.QQV, new YFSourceQQV());
    }};

    public BigDecimal getCurrent(Ticker ticker) throws IOException {
        try {
            return primary.get(ticker).getCurrent();
        } catch (IOException e) {
            return backup.get(ticker).getCurrent();
        }
    }

    public HiLo get52wRange(Ticker ticker) throws IOException {
        Source p = primary.get(ticker);
        Source b = backup.get(ticker);

        if (p.is52wRangeSupported()) {
            try {
                return p.get52wRange();
            } catch (IOException e) {
                if (b.is52wRangeSupported()) {
                    return b.get52wRange();
                }
            }
        } else {
            return b.get52wRange();
        }
        return null;
    }
}
