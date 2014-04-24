package co.kuznetsov.market.monitor;

import com.sleepycat.je.*;

import java.io.File;
import java.math.BigDecimal;

public class Fixing {
    private final static String STORAGE_PATH = initStoragePath();

    public void fixQuote(Ticker ticker, BigDecimal current) {
        try {
            EnvironmentConfig envConf = new EnvironmentConfig();
            envConf.setAllowCreate(true);
            envConf.setTransactional(false);

            DatabaseConfig dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            dbConfig.setTransactional(false);
            dbConfig.setDeferredWrite(true);

            try (Environment dbEnv = new Environment(new File(STORAGE_PATH), envConf);
                 Database db = dbEnv.openDatabase(null, "quote-fixing", dbConfig)) {
                db.put(null,
                       new DatabaseEntry(ticker.name().getBytes()),
                       new DatabaseEntry(current.toString().getBytes()));
                db.sync();
                dbEnv.sync();
                dbEnv.compress();
                dbEnv.cleanLog();
            }
        } catch (Exception dbe) {
            dbe.printStackTrace();
        }
    }

    public void fixWarnLevel(int warnLevel) {
        try {
            EnvironmentConfig envConf = new EnvironmentConfig();
            envConf.setAllowCreate(true);
            envConf.setTransactional(false);

            DatabaseConfig dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            dbConfig.setTransactional(false);
            dbConfig.setDeferredWrite(true);

            try (Environment dbEnv = new Environment(new File(STORAGE_PATH), envConf);
                 Database db = dbEnv.openDatabase(null, "wl-fixing", dbConfig)) {
                db.put(null,
                        new DatabaseEntry("$WARN_LEVEL".getBytes()),
                        new DatabaseEntry(Integer.toString(warnLevel).getBytes()));
                db.sync();
                dbEnv.sync();
                dbEnv.compress();
                dbEnv.cleanLog();
            }
        } catch (Exception dbe) {
            dbe.printStackTrace();
        }
    }

    public BigDecimal getQuote(Ticker ticker) {
        try {
            EnvironmentConfig envConf = new EnvironmentConfig();
            envConf.setAllowCreate(true);
            envConf.setTransactional(false);

            DatabaseConfig dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            dbConfig.setTransactional(false);
            dbConfig.setDeferredWrite(true);

            try (Environment dbEnv = new Environment(new File(STORAGE_PATH), envConf);
                 Database db = dbEnv.openDatabase(null, "quote-fixing", dbConfig)) {
                DatabaseEntry data = new DatabaseEntry();
                OperationStatus os = db.get(null,
                                            new DatabaseEntry(ticker.name().getBytes()),
                                            data,
                                            LockMode.READ_COMMITTED);
                switch (os) {
                    case SUCCESS:
                        return new BigDecimal(new String(data.getData()));
                    default:
                        return null;
                }
            }
        } catch (Exception dbe) {
            dbe.printStackTrace();
            return null;
        }
    }

    public Integer getWarnLevel() {
        try {
            EnvironmentConfig envConf = new EnvironmentConfig();
            envConf.setAllowCreate(true);
            envConf.setTransactional(false);

            DatabaseConfig dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            dbConfig.setTransactional(false);
            dbConfig.setDeferredWrite(true);

            try (Environment dbEnv = new Environment(new File(STORAGE_PATH), envConf);
                 Database db = dbEnv.openDatabase(null, "wl-fixing", dbConfig)) {
                DatabaseEntry data = new DatabaseEntry();
                OperationStatus os = db.get(null,
                        new DatabaseEntry("$WARN_LEVEL".getBytes()),
                        data,
                        LockMode.READ_COMMITTED);
                switch (os) {
                    case SUCCESS:
                        return new Integer(new String(data.getData()));
                    default:
                        return null;
                }
            }
        } catch (Exception dbe) {
            dbe.printStackTrace();
            return null;
        }
    }

    private static String initStoragePath() {
        String home = System.getProperty("user.home");
        File f = new File(home + File.separator + ".market-monitor");
        if (!f.exists()) {
            if (!f.mkdirs()) {
                throw new IllegalStateException("Unable to init storage directory: "+f.getAbsolutePath());
            }
        } else {
            if (!f.isDirectory()) {
                throw new IllegalStateException(f.getAbsolutePath() + " is not a directory!");
            }
        }

        return f.getAbsolutePath();
    }
}
