package co.kuznetsov.market.monitor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
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
        int max = 0;
        int maxFixing = 0;
        for(Spread s : spreads) {
            BigDecimal current = quoteHolder.getCurrent(s.getTicker());
            BigDecimal fixing = quoteHolder.getLastFixing(s.getTicker());
            int level = getWarningLevel(s, current);
            int fixingLevel = getWarningLevel(s, fixing);
            if (max < level) {
                max = level;
            }
            if (maxFixing < fixingLevel) {
                maxFixing = fixingLevel;
            }
        }

        return new WarnLevel(max, max - maxFixing, quoteHolder.isMarketOpen());
    }

    private int getWarningLevel(Spread s, BigDecimal current) {
        BigDecimal deltaLo = current.subtract(s.getLo());
        BigDecimal deltaHi = s.getHi().subtract(current);
        BigDecimal spread  = s.getHi().subtract(s.getLo());

        if (deltaLo.compareTo(BigDecimal.ZERO) <= 0) {
            return MAX_WARNING;
        }
        if (deltaHi.compareTo(BigDecimal.ZERO) <= 0) {
            return MAX_WARNING;
        }
        BigDecimal spDiv2 = spread.divide(BigDecimal.valueOf(2));

        MathContext digit4 = new MathContext(4);

        BigDecimal wLo;
        if (deltaLo.compareTo(spDiv2) <= 0) {
            wLo = BigDecimal.TEN.multiply(BigDecimal.ONE.subtract(deltaLo.divide(spDiv2, digit4)));
        } else {
            wLo = BigDecimal.ZERO;
        }

        BigDecimal wHi;
        if (deltaHi.compareTo(spDiv2) <= 0) {
            wHi = BigDecimal.TEN.multiply(BigDecimal.ONE.subtract(deltaHi.divide(spDiv2, digit4)));
        } else {
            wHi = BigDecimal.ZERO;
        }

        BigDecimal point5 = new BigDecimal("0.5");
        if (wHi.compareTo(point5) < 0 && wLo.compareTo(point5) < 0) {
            return 0;
        } else {
            return Math.max(round(wHi), round(wLo));
        }
    }

    private int round(BigDecimal val) {
        return val.setScale(0, BigDecimal.ROUND_UP).intValueExact();
    }
}


