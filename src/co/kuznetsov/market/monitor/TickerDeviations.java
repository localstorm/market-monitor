package co.kuznetsov.market.monitor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by localstorm on 4/5/14.
 */
public class TickerDeviations {
    private Map<Ticker, BigDecimal> deviations = new ConcurrentHashMap<>();

    public BigDecimal getDeviation(Ticker ticker) {
        switch (ticker) {
            case SNP: return new BigDecimal("1.1");
            default:  return null;
        }
    }
}
