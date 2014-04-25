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
public class GFSourceRVX implements Source {

    @Override
    public BigDecimal getCurrent() throws IOException {
        Document doc = Jsoup.parse(new URL("https://www.google.com/finance?q=INDEXCBOE%3ARVX"), 10000);
        Elements elements = doc.getElementsByAttributeValue("id", "ref_22304677664342_l");
        if (elements.isEmpty()) {
            throw new IOException("Unable to extract "+getTicker());
        } else {
            for (Element e: elements) {
                if (e.nodeName().equalsIgnoreCase("span")) {
                    String txt = e.text();
                    txt = txt.replace(",", "");
                    return sanity(getTicker().name(), new BigDecimal(txt), 2, 100);
                }
            }
            throw new IOException("Unable to extract "+getTicker());
        }
    }

    @Override
    public Ticker getTicker() {
        return Ticker.RVX;
    }
}
