package co.kuznetsov.market.feeds.yahoo;

import co.kuznetsov.market.feeds.Source;
import co.kuznetsov.market.feeds.HiLo;
import co.kuznetsov.market.monitor.Ticker;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class YFSourceOEX implements Source {

    public static final String TICKER_URL = "http://finance.yahoo.com/q?s=%5EOEX";

    @Override
    public BigDecimal getCurrent() throws IOException {
        return YFUtil.getCurrent(TICKER_URL,
                "yfs_l10_^oex",
                Ticker.OEX,
                100,
                2000);
    }

    @Override
    public Ticker getTicker() {
        return Ticker.OEX;
    }

    @Override
    public HiLo get52wRange() throws IOException {
        return null;
    }

    @Override
    public boolean is52wRangeSupported() {
        return true;
    }
}
