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
public class SourceVIX implements Source {
    @Override
    public BigDecimal getCurrent() throws IOException {
        Document doc = Jsoup.parse(new URL("http://finance.yahoo.com/q?s=%5EVIX"), 10000);
        Elements elements = doc.getElementsByAttributeValue("id", "yfs_l10_^vix");
        if (elements.isEmpty()) {
            throw new IOException("Unable to extract VIX");
        } else {
            for (Element e: elements) {
                if (e.nodeName().equalsIgnoreCase("span")) {
                    String txt = e.text();
                    txt = txt.replace(",", "");
                    return sanity("VIX", new BigDecimal(txt), 5,100);
                }
            }
            throw new IOException("Unable to extract VIX");
        }
    }

    @Override
    public Ticker getTicker() {
        return Ticker.VIX;
    }
}
