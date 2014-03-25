package co.kuznetsov.market.monitor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class SpreadHolder {
    private final static int MAX_WARNING = 10;

    private List<Spread> spreads = new CopyOnWriteArrayList<>();

    public void addSpread(Spread sp) {
        spreads.add(sp);
    }

    public void removeSpreads() {
        spreads.clear();
    }

    public WarnLevel getWarnLevel(QuoteHolder quoteHolder) {
        int cur = 0;
        Map<Integer, TreeSet<Spread>> levels = new TreeMap<>(Collections.reverseOrder());
        for(Spread s : spreads) {
            BigDecimal current = quoteHolder.getCurrent(s.getTicker());
            int level = getWarningLevel(s, current);
            TreeSet<Spread> tickers = levels.get(level);
            if (tickers == null) {
                tickers = new TreeSet<>();
                levels.put(level, tickers);
            }
            tickers.add(s);
            if (cur < level) {
                cur = level;
            }
        }
        int prevLevel = quoteHolder.getLastWarningLevel(cur);
        boolean open = quoteHolder.isMarketOpen();
        if (quoteHolder.isCanFixWarnLevel() && quoteHolder.getLastWarningLevel(cur) != cur) {
            quoteHolder.fixWarnLevel(cur);
            prevLevel = cur;
        }

        for (Integer level: levels.keySet()) {
            for (Spread spread: levels.get(level)) {
                System.out.println("[" + level + "]:\t" + spread);
            }
        }
        return new WarnLevel(cur, cur - prevLevel, open);
    }

    private int getWarningLevel(Spread s, BigDecimal current) {
        BigDecimal deltaLo = current.subtract(s.getLo());
        BigDecimal deltaHi = s.getHi().subtract(current);
        BigDecimal spread  = s.getHi().subtract(s.getLo());

        if (deltaLo.compareTo(BigDecimal.ZERO) <= 0 ||
            deltaHi.compareTo(BigDecimal.ZERO) <= 0) {
            return MAX_WARNING;
        }

        MathContext digit4 = new MathContext(4);
        BigDecimal spDiv2 = spread.divide(BigDecimal.valueOf(2));

        BigDecimal wLo;
        BigDecimal wHi;

        if (deltaLo.compareTo(spDiv2) <= 0) {
            wLo = BigDecimal.TEN.multiply(BigDecimal.ONE.subtract(deltaLo.divide(spDiv2, digit4)));
        } else {
            wLo = BigDecimal.ZERO;
        }

        if (deltaHi.compareTo(spDiv2) <= 0) {
            wHi = BigDecimal.TEN.multiply(BigDecimal.ONE.subtract(deltaHi.divide(spDiv2, digit4)));
        } else {
            wHi = BigDecimal.ZERO;
        }

        BigDecimal point5 = new BigDecimal("0.5");
        if (wHi.compareTo(point5) >= 0 || wLo.compareTo(point5) >= 0) {
            return Math.max(round(wHi), round(wLo));
        } else {
            return 0;
        }
    }

    private int round(BigDecimal val) {
        return val.setScale(0, BigDecimal.ROUND_UP).intValueExact();
    }
}


