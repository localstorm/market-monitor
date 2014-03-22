package co.kuznetsov.market.monitor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicLong;

public class MarketHash {

    private final AtomicLong currentChecksum = new AtomicLong(0);
    private final AtomicLong lastUpdate = new AtomicLong(0);

    public MarketHash(Fixing fixing) {
        StringBuilder sb = new StringBuilder();
        for (Ticker v : Ticker.values()) {
            sb.append(fixing.getQuote(v));
            sb.append('|');
        }
        hash(sb);
    }

    private void hash(StringBuilder sb) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] hash = md.digest(sb.toString().getBytes());
            long newChecksum = new BigInteger(1, hash).longValue();
            if (currentChecksum.get() !=0 && newChecksum != currentChecksum.get()) {
                lastUpdate.set(System.currentTimeMillis());
            }
            currentChecksum.set(newChecksum);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public long getLastUpdate() {
        return lastUpdate.get();
    }

    public void update(QuoteHolder quoteHolder) {
        StringBuilder sb = new StringBuilder();
        for (Ticker v : Ticker.values()) {
            BigDecimal quote = quoteHolder.getCurrent(v);
            if (quote == null) {
                quote = quoteHolder.getLastFixing(v);
            }
            sb.append(quote);
            sb.append('|');
        }
        hash(sb);
    }
}
