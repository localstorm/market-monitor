package co.kuznetsov.market.monitor;

import co.kuznetsov.market.feeds.HiLo;
import co.kuznetsov.market.feeds.Source;
import co.kuznetsov.market.feeds.google.GFSourceSNP;
import co.kuznetsov.market.feeds.yahoo.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class MarketMonitor {
    private SpreadHolder spreadHolder               = new SpreadHolder();
    private QuoteHolder quoteHolder                 = new QuoteHolder();
    private DeviationMonitor deviationMonitor       = new DeviationMonitor(quoteHolder);
    private AtomicReference<WarnLevel> WARN_LEVEL   = new AtomicReference<>(null);
    private SourceChain sourceChain                 = new SourceChain();

    private String[] spreadsPaths;


    public void refreshStatic() {
        reloadRanges();
    }

    public WarnLevel getCurrentWarnLevel() throws IOException {
        reloadSpreads();

        reloadQuotes();
        WarnLevel newLevel = spreadHolder.getWarnLevel(quoteHolder);
        if (WARN_LEVEL.get() != null && Math.abs(WARN_LEVEL.get().getLevel() - newLevel.getLevel()) >= 2) {
            reloadQuotes();
            newLevel = spreadHolder.getWarnLevel(quoteHolder);
        }
        WARN_LEVEL.set(newLevel);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream buf = new PrintStream(baos);

        beginOutput(buf);
        if (quoteHolder.isMarketOpen()) {
            quoteHolder.printQuotes(buf, true, true);
            deviationMonitor.printDeviations(buf, true, false);
            deviationMonitor.printDeviationsPct(buf, true, false);
            quoteHolder.printQuotes(buf, false, true);
            deviationMonitor.printDeviations(buf, false, false);
            deviationMonitor.printDeviationsPct(buf, false, false);
            deviationMonitor.printIVRanks(buf);
        } else {
            quoteHolder.printQuotes(buf, true, true);
            quoteHolder.printQuotes(buf, false, true);
            deviationMonitor.printIVRanks(buf);
        }
        endOutput(buf);
        spreadHolder.printSpreads(buf, quoteHolder);

        buf.close();
        System.out.print(baos.toString());
        return newLevel;
    }


    private void beginOutput(PrintStream out) {
        String opn = String.format("%s", (quoteHolder.isMarketOpen() ? "open" : "closed"));
        out.println("----["+now()+":"+opn+"]----------------------------------------------->");
    }

    private void endOutput(PrintStream out) {
        out.println("<-------------------------------------------------------------------");
    }

    private void reloadSpreads() throws IOException  {
        spreadHolder.removeSpreads();
        SpreadsParser sp = new SpreadsParser();
        for (String path: spreadsPaths) {
            List<Spread> spreads = sp.parse(new File(path));
            for (Spread s : spreads) {
                spreadHolder.addSpread(s);
            }
        }
    }

    private void reloadQuotes() throws IOException {
        ArrayList<Thread> threads = new ArrayList<>();
        for (Ticker t: Ticker.values()) {
            final Ticker tt = t;
            Thread th = new Thread() {
                @Override
                public void run() {
                    try {
                        loadWithRetry(tt);
                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                    }
                }
            };
            th.setDaemon(true);
            th.start();
            threads.add(th);
        }
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                t.stop();
            }
        }
    }

    private void reloadRanges() {
        ArrayList<Thread> threads = new ArrayList<>();
        for (Ticker t: Ticker.values()) {
            if (!t.isVolatility()) {
                continue;
            }
            final Ticker tt = t;
            Thread th = new Thread() {
                @Override
                public void run() {
                    try {
                        load52wRangesWithRetry(tt);
                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                    }
                }
            };
            th.setDaemon(true);
            th.start();
            threads.add(th);
        }
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                t.stop();
            }
        }

    }

    public void setSpreadsPaths(String[] spreadsPath) {
        this.spreadsPaths = spreadsPath;
    }

    private void loadWithRetry(Ticker ticker) throws IOException {
        int retry = 5;
        do {
            try {
                retry--;
                quoteHolder.update(ticker, sourceChain.getCurrent(ticker));
                return;
            } catch(Exception e) {
                if (retry == 0) {
                    throw e;
                }
            }
        } while (true);
    }

    private void load52wRangesWithRetry(Ticker ticker) throws IOException {
        int retry = 5;
        do {
            try {
                retry--;
                HiLo range = sourceChain.get52wRange(ticker);
                if (range != null) {
                    quoteHolder.update52wRange(ticker, range);
                }
                return;
            } catch(Exception e) {
                if (retry == 0) {
                    throw e;
                }
            }
        } while (true);
    }


    private String now() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(now);
    }
}
