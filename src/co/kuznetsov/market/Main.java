package co.kuznetsov.market;

import co.kuznetsov.market.gui.Alerter;
import co.kuznetsov.market.monitor.MarketMonitor;
import co.kuznetsov.market.monitor.WarnLevel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class Main {
    public static final String APP_NAME = "Market Monitor";
    public static final long REFRESH_LOOP_OPEN   = 5000;
    public static final long REFRESH_LOOP_CLOSED = 60000;

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.err.println("Usage: <path to market config>");
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
        marketMonitor.setSpreadsPath(args[0]);

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
        item.setImage(alerter.getImage());
        item.setHighlightImage(alerter.getImage());
        item.setToolTip(tip);


        Thread watcher = new Thread() {
            @Override
            public void run() {
                try {
                    while (!this.isInterrupted()) {
                        try {
                            final WarnLevel wl = marketMonitor.getCurrentWarnLevel();
                            if (alerter.status(display, wl)) {
                                Display.getDefault().syncExec(new Runnable() {
                                    public void run() {
                                        item.setToolTipText("Options DEFCON (" + marketStatus(wl) + ")");
                                        item.setImage(alerter.getImage());
                                        item.setHighlightImage(alerter.getImage());
                                        tip.setVisible(false);
                                    }
                                });
                            }
                            Thread.sleep(wl.isMarketOpen() ? REFRESH_LOOP_OPEN : REFRESH_LOOP_CLOSED);
                        } catch (Exception e) {
                            alerter.offline(display);
                            e.printStackTrace(System.err);
                            Display.getDefault().syncExec(new Runnable() {
                                public void run() {
                                    item.setToolTipText("Options DEFCON (ERROR!)");
                                    item.setImage(alerter.getImage());
                                    item.setHighlightImage(alerter.getImage());
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
        alerter.dispose();
        display.dispose();
    }

    private static String marketStatus(WarnLevel wl) {
        if (wl.isMarketOpen()) {
            return  wl.getLevel() + ", market is open";
        } else {
            return wl.getLevel() + ", market is closed";
        }
    }
}
