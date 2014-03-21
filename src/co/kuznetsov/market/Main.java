package co.kuznetsov.market;

import co.kuznetsov.market.market.MarketMonitor;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: <path to market config>");
            return;
        }

        final MarketMonitor marketMonitor = new MarketMonitor();
        marketMonitor.setConfigPath(args[0]);

        final Display display = new Display();
        Shell shell = new Shell(display);
        Image image = new Image(display, 16, 16);

        final Alerter alerter = new Alerter(display);
        alerter.status(display, 0);

        final Tray tray = display.getSystemTray();
        if (tray == null) {
            System.err.println("The system tray is not available");
        } else {
            final ToolTip tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
            tip.setMessage("Unable to reload quotes/spreads!");
            final TrayItem item = new TrayItem(tray, SWT.NONE);
            item.setToolTipText("Options DEFCON");

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
            item.setHighlightImage(image);
            item.setToolTip(tip);


            Thread watcher = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!this.isInterrupted()) {
                            try {
                                int alertLevel = marketMonitor.evalDangerLevel();
                                alerter.status(display, alertLevel);
                                Display.getDefault().syncExec(new Runnable() {
                                    public void run() {
                                        item.setImage(alerter.getImage());
                                        tip.setVisible(false);
                                    }
                                });
                                Thread.sleep(5000);
                            } catch (Exception e) {
                                alerter.offline(display);
                                e.printStackTrace(System.err);
                                Display.getDefault().syncExec(new Runnable() {
                                    public void run() {
                                        item.setImage(alerter.getImage());
                                        tip.setVisible(true);
                                    }
                                });
                                Thread.sleep(30000);
                            }
                        }
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }
            };

            watcher.start();
        }

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }

        image.dispose();
        alerter.dispose();
        display.dispose();
    }
}
