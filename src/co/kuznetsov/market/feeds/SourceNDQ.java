package co.kuznetsov.market.feeds;

import co.kuznetsov.market.market.Ticker;
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
public class SourceNDQ implements Source {
    @Override
    public BigDecimal getCurrent() throws IOException {
        Document doc = Jsoup.parse(new URL("http://finance.yahoo.com/q?s=%5EIXIC"), 10000);
        Elements elements = doc.getElementsByAttributeValue("id", "yfs_l10_^ixic");
        if (elements.isEmpty()) {
            throw new IOException("Unable to extract NDX");
        } else {
            for (Element e: elements) {
                if (e.nodeName().equalsIgnoreCase("span")) {
                    String txt = e.text();
                    txt = txt.replace(",", "");
                    return sanity("NDX", new BigDecimal(txt), 1000,10000);
                }
            }
            throw new IOException("Unable to extract NDX");
        }
    }

    @Override
    public Ticker getTicker() {
        return Ticker.NDQ;
    }
}
