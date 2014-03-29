package co.kuznetsov.market;

import co.kuznetsov.market.gui.Alerter;
import co.kuznetsov.market.monitor.MarketMonitor;
import co.kuznetsov.market.monitor.WarnLevel;
import co.kuznetsov.market.sound.Player;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class Main {
    public static final String MARKET_CRITICAL_SOUND_RESOURCE = "critical.wav";
    public static final String MARKET_BELL_SOUND_RESOURCE = "bell.wav";
    public static final String MUTE_SYSTEM_PROPERTY = "market-monitor.mute";

    public static final String APP_NAME = "Market Monitor";

    public static final int CRITICAL_WARN_THRESHOLD = 8;
    public static final long REFRESH_LOOP_OPEN = 5000;
    public static final long REFRESH_LOOP_CLOSED = 60000;

    public static void main(String[] args) throws Exception {
        final boolean mute = Boolean.parseBoolean(System.getProperty(MUTE_SYSTEM_PROPERTY));
        System.out.println("Sound: " + (mute ? "off" : "on"));

        if (args.length == 0) {
            System.err.println("Usage: <paths to spread configs>");
            return;
        }

        Display.setAppName(APP_NAME);
        final Display display = Display.getDefault();

        // TODO: init app menu for Mac

        Shell shell = new Shell(display);
        shell.setText(APP_NAME);

        final Alerter alerter = new Alerter(display);
        alerter.status(display, new WarnLevel(0, 0, false));

        final MarketMonitor marketMonitor = new MarketMonitor();
        marketMonitor.setSpreadsPaths(args);

        final Tray tray = display.getSystemTray();
        if (tray == null) {
            System.err.println("The system tray is not available");
            return;
        }

        final ToolTip tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
        tip.setMessage("Unable to reload quotes/spreads!");
        final TrayItem item = new TrayItem(tray, SWT.NONE);

        final Menu menu = new Menu(shell, SWT.POP_UP);
        MenuItem mi = new MenuItem(menu, SWT.PUSH);
        mi.setText("Exit");
        mi.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                item.setVisible(false);
                menu.setVisible(false);
                System.exit(0);
            }
        });

        item.addListener(SWT.MenuDetect, new Listener() {
            @Override
            public void handleEvent(Event event) {
                menu.setVisible(true);
            }
        });
        updateIcon(item, alerter.offline(display));
        item.setToolTip(tip);


        Thread watcher = new Thread() {
            @Override
            public void run() {
                try {
                    boolean marketOpen = false;
                    while (!this.isInterrupted()) {
                        try {
                            final WarnLevel wl = marketMonitor.getCurrentWarnLevel();
                            marketOpenClosedSound(marketOpen != wl.isMarketOpen(), mute);
                            dangerousZoneAlarm(wl.isMarketOpen(), wl.getLevel(), mute);
                            marketOpen = wl.isMarketOpen();
                            final Image img = alerter.status(display, wl);
                            if (img != null) {
                                Display.getDefault().syncExec(new Runnable() {
                                    public void run() {
                                        item.setToolTipText("Options DEFCON (" + marketStatus(wl) + ")");
                                        updateIcon(item, img);
                                        tip.setVisible(false);
                                    }
                                });
                            }
                            Thread.sleep(wl.isMarketOpen() ? REFRESH_LOOP_OPEN : REFRESH_LOOP_CLOSED);
                        } catch (Exception e) {
                            e.printStackTrace(System.err);
                            final Image img = alerter.offline(display);
                            Display.getDefault().syncExec(new Runnable() {
                                public void run() {
                                    item.setToolTipText("Options DEFCON (ERROR!)");
                                    updateIcon(item, img);
                                    tip.setVisible(true);
                                }
                            });
                            Thread.sleep(REFRESH_LOOP_OPEN);
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
        };

        watcher.start();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }

        watcher.interrupt();
        watcher.join(10000);
        display.dispose();
    }

    private static void updateIcon(TrayItem item, Image ico) {
        Image old = item.getImage();
        item.setImage(ico);
        item.setHighlightImage(ico);
        if (old != null) {
            old.dispose();
        }
    }

    private static void dangerousZoneAlarm(boolean marketOpen, int level, boolean mute) {
        if (marketOpen && level >= CRITICAL_WARN_THRESHOLD && !mute) {
            Player.playAsync(MARKET_CRITICAL_SOUND_RESOURCE);
        }
    }

    private static void marketOpenClosedSound(boolean change, boolean mute) {
        if (change && !mute) {
            Player.playAsync(MARKET_BELL_SOUND_RESOURCE);
        }
    }

    private static String marketStatus(WarnLevel wl) {
        if (wl.isMarketOpen()) {
            return wl.getLevel() + ", market is open";
        } else {
            return wl.getLevel() + ", market is closed";
        }
    }
}
