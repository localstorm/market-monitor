package co.kuznetsov.market.feeds.google;

import co.kuznetsov.market.feeds.HiLo;
import co.kuznetsov.market.feeds.SanityUtils;
import co.kuznetsov.market.monitor.Ticker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import static co.kuznetsov.market.feeds.SanityUtils.sanity;

/**
 * Created by localstorm on 4/27/14.
 */
public class GFUtil {

    private final static ConcurrentHashMap<String, Document> REUSE_CACHE = new ConcurrentHashMap<>();

    private static Document get(String url) throws IOException {
        Document doc = REUSE_CACHE.remove(url);
        if (doc == null) {
            doc = Jsoup.parse(new URL(url), 10000);
            REUSE_CACHE.put(url, doc);
            return doc;
        } else {
            return doc;
        }
    }

    public static BigDecimal getCurrent(String url, String id, Ticker ticker, int sanityMin, int sanityMax) throws IOException {
        Elements elements = get(url).getElementsByAttributeValue("id", id);
        if (elements.isEmpty()) {
            throw new IOException("Unable to extract "+ticker);
        } else {
            for (Element e: elements) {
                if (e.nodeName().equalsIgnoreCase("span")) {
                    String txt = e.text();
                    txt = txt.replace(",", "");
                    return sanity(ticker.name(), new BigDecimal(txt), sanityMin, sanityMax);
                }
            }
            throw new IOException("Unable to extract "+ticker);
        }
    }

    public static HiLo get52wRange(String url, Ticker ticker, int sanityMin, int sanityMax) throws IOException {
        try {
            Elements elements = get(url).getElementsByAttributeValue("data-snapfield", "range_52week");
            elements = elements.first().parent().getElementsByAttributeValue("class", "val");
            String text = elements.first().getElementsByTag("td").text();
            String lohi[] = text.split(" - ");
            HiLo result = new HiLo(new BigDecimal(lohi[1]), new BigDecimal(lohi[0]));
            SanityUtils.sanity(ticker.name(), result.getHigh(), sanityMin, sanityMax);
            SanityUtils.sanity(ticker.name(), result.getLow(), sanityMin, sanityMax);
            return result;
        } catch(Exception e) {
            throw new IOException(e);
        }
    }
}
