package co.kuznetsov.market.feeds.yahoo;

import co.kuznetsov.market.feeds.Source;
import co.kuznetsov.market.feeds.HiLo;
import co.kuznetsov.market.monitor.Ticker;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 25.03.14
 */
public class YFSourceQQV implements Source {

    public static final String TICKER_URL = "http://finance.yahoo.com/q?s=%5EQQV";

    @Override
    public BigDecimal getCurrent() throws IOException {
        return YFUtil.getCurrent(TICKER_URL,
                "yfs_l10_^qqv",
                Ticker.QQV,
                2,
                100);
    }

    @Override
    public Ticker getTicker() {
        return Ticker.QQV;
    }

    @Override
    public HiLo get52wRange() throws IOException {
        return YFUtil.get52wRange(TICKER_URL, getTicker(), 2, 100);
    }

    @Override
    public boolean is52wRangeSupported() {
        return true;
    }
}
