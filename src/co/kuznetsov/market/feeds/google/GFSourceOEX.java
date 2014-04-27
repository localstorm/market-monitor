package co.kuznetsov.market.feeds.google;

import co.kuznetsov.market.feeds.Source;
import co.kuznetsov.market.feeds.HiLo;
import co.kuznetsov.market.monitor.Ticker;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class GFSourceOEX implements Source {

    @Override
    public BigDecimal getCurrent() throws IOException {
        return GFUtil.getCurrent("https://www.google.com/finance?q=INDEXCBOE%3AOEX",
                "ref_1000736529757159_l",
                Ticker.OEX,
                100,
                4000);
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
        return false;
    }
}
