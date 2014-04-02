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
    private final static int MIN_WARNING = 0;

    private List<Spread> spreads = new CopyOnWriteArrayList<>();

    public void addSpread(Spread sp) {
        spreads.add(sp);
    }

    public void removeSpreads() {
        spreads.clear();
    }

    public WarnLevel getWarnLevel(QuoteHolder quoteHolder) {
        int cur = 0;
        Map<Integer, Set<Spread>> levels = new TreeMap<>(Collections.reverseOrder());
        for(Spread s : spreads) {
            BigDecimal current = quoteHolder.getCurrent(s.getTicker());
            int level = getWarningLevel(s, current);
            Set<Spread> tickers = levels.get(level);
            if (tickers == null) {
                tickers = new HashSet<>();
                levels.put(level, tickers);
            }
            tickers.add(s);
            if (cur < level) {
                cur = level;
            }
        }
        int prevLevel = quoteHolder.getLastWarningLevel();
        boolean open = quoteHolder.isMarketOpen();
        if (quoteHolder.isCanFixWarnLevel() && prevLevel != cur) {
            quoteHolder.fixWarnLevel(cur);
            prevLevel = cur;
        }

        for (Integer level: levels.keySet()) {
            ArrayList<Spread> spreads  = new ArrayList<>(levels.get(level));
            Collections.sort(spreads);
            for (Spread spread: spreads) {
                System.out.printf("[%d]:"+(level == 10 ? " " : "  ")+"%s\n", level, spread);
            }
        }
        if (prevLevel >= 0) {
            return new WarnLevel(cur, cur - prevLevel, open);
        } else {
            return new WarnLevel(cur, 0, open);
        }
    }

    private int getWarningLevel(Spread s, BigDecimal current) {
        if (s.getExpiration() != null && s.getExpiration().daysLeft() < 0) {
            return MIN_WARNING;
        }

        BigDecimal deltaLo = current.subtract(s.getLo());
        BigDecimal deltaHi = s.getHi().subtract(current);
        BigDecimal loHalf  = s.getMid().subtract(s.getLo());
        BigDecimal hiHalf  = s.getHi().subtract(s.getMid());

        if (deltaLo.compareTo(BigDecimal.ZERO) <= 0 ||
            deltaHi.compareTo(BigDecimal.ZERO) <= 0) {
            return MAX_WARNING;
        }

        MathContext digit4 = new MathContext(4);

        BigDecimal wLo;
        BigDecimal wHi;

        if (deltaLo.compareTo(loHalf) <= 0) {
            wLo = BigDecimal.TEN.multiply(BigDecimal.ONE.subtract(deltaLo.divide(loHalf, digit4)));
        } else {
            wLo = BigDecimal.ZERO;
        }

        if (deltaHi.compareTo(hiHalf) <= 0) {
            wHi = BigDecimal.TEN.multiply(BigDecimal.ONE.subtract(deltaHi.divide(hiHalf, digit4)));
        } else {
            wHi = BigDecimal.ZERO;
        }

        BigDecimal point5 = new BigDecimal("0.5");
        if (wHi.compareTo(point5) >= 0 || wLo.compareTo(point5) >= 0) {
            return Math.max(round(wHi), round(wLo));
        } else {
            return MIN_WARNING;
        }
    }

    private int round(BigDecimal val) {
        return val.setScale(0, BigDecimal.ROUND_UP).intValueExact();
    }
}


