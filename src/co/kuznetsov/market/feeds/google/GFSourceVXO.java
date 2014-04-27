package co.kuznetsov.market.feeds.google;

import co.kuznetsov.market.feeds.Source;
import co.kuznetsov.market.monitor.Ticker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import static co.kuznetsov.market.feeds.SanityUtils.sanity;

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
    public BigDecimal getRank() throws IOException {
        return null;
    }

    @Override
    public boolean isRankSupported() {
        return true;
    }
}
