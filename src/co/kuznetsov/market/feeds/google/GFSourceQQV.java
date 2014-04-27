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
public class GFSourceQQV implements Source {

    @Override
    public BigDecimal getCurrent() throws IOException {
        return GFUtil.getCurrent("https://www.google.com/finance?q=INDEXNYSEGIS%3AQQV",
                "ref_13628789_l",
                Ticker.QQV,
                2,
                100);
    }

    @Override
    public Ticker getTicker() {
        return Ticker.QQV;
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
