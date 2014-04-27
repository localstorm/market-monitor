package co.kuznetsov.market.feeds.yahoo;

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
public class YFSourceSNP implements Source {

    @Override
    public BigDecimal getCurrent() throws IOException {
        return YFUtil.getCurrent("http://finance.yahoo.com/q?s=%5EGSPC",
                "yfs_l10_^gspc",
                Ticker.SNP,
                500,
                5000);
    }

    @Override
    public Ticker getTicker() {
        return Ticker.SNP;
    }

    @Override
    public BigDecimal getRank() throws IOException {
        return null;
    }

    @Override
    public boolean isRankSupported() {
        return false;
    }
}
