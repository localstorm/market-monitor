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
public class GFSourceVXO implements Source {

    @Override
    public BigDecimal getCurrent() throws IOException {
        return GFUtil.getCurrent("https://www.google.com/finance?q=INDEXCBOE%3AVXO",
                "ref_915665049136660_l",
                Ticker.VXO,
                2,
                100);
    }

    @Override
    public Ticker getTicker() {
        return Ticker.VXO;
    }

    @Override
    public HiLo get52wRange() throws IOException {
        return GFUtil.get52wRange(
                "https://www.google.com/finance?q=INDEXCBOE%3AVXO",
                Ticker.VXO,
                2,
                100
        );
    }

    @Override
    public boolean is52wRangeSupported() {
        return true;
    }
}
