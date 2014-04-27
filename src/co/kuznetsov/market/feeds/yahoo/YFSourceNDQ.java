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
public class YFSourceNDQ implements Source {

    @Override
    public BigDecimal getCurrent() throws IOException {
        return YFUtil.getCurrent("http://finance.yahoo.com/q?s=%5EIXIC",
                                 "yfs_l10_^ixic",
                                 Ticker.NDQ,
                                 500,
                                 10000);
    }

    @Override
    public Ticker getTicker() {
        return Ticker.NDQ;
    }

    @Override
    public HiLo get52wRange() throws IOException {
        return null;
    }

    @Override
    public boolean is52wRangeSupported() {
        return false;
    }
}
