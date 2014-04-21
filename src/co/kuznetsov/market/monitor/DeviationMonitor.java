package co.kuznetsov.market.monitor;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by localstorm on 4/5/14.
 */
public class DeviationMonitor {
    private TickerDeviations tickerDeviations;
    private QuoteHolder quoteHolder;

    public DeviationMonitor(QuoteHolder quoteHolder, TickerDeviations tickerDeviations) {
        this.quoteHolder = quoteHolder;
        this.tickerDeviations = tickerDeviations;
    }

    public Deviation getHighestDeviation() {
        ArrayList<Deviation> deviations = new ArrayList<>();
        for (Ticker ticker: Ticker.values()) {
            deviations.add(quoteHolder.getDeviation(ticker));
        }
        Collections.sort(deviations);
        for (Deviation d: deviations) {
            d.setOpportunity(true);
        }
        return deviations.get(0);
    }

    public void printOpportunities() {
        System.out.println(getHighestDeviation());
    }
}
