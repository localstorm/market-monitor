package co.kuznetsov.market.gui;

import co.kuznetsov.market.monitor.WarnLevel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class Alerter {

    private int [] RED      = {0xFF, 0x45, 0};
    private int [] YELLOW   = {0xFF, 0xFF, 0x66};
    private int [] GREEN    = {0, 0x80, 0};

    private AtomicInteger currentDelta  = new AtomicInteger(0);
    private AtomicInteger currentLevel  = new AtomicInteger(0);
    private AtomicBoolean currentMarket = new AtomicBoolean(false);

    private Image alerter;
    private GC gc;

    public Alerter(Display display) {
        alerter = new Image(display, 16, 16);
        gc = new GC(alerter);
        offline(display);
    }

    public boolean status(Display display, WarnLevel wl) {
        Color c = null;
        int warnLevel = wl.getLevel();

        if (warnLevel == 5) {
            c = new Color(display, YELLOW[0], YELLOW[1], YELLOW[2]);
        }

        if (warnLevel < 5) {
            int danger = warnLevel;
            int safetyLevel = 5 - danger;
            c = new Color(display, (int) ((0.2 * danger) * YELLOW[0] +  (0.2 * safetyLevel) * GREEN[0]),
                                   (int) ((0.2 * danger) * YELLOW[1] +  (0.2 * safetyLevel) * GREEN[1]),
                                   (int) ((0.2 * danger) * YELLOW[2] +  (0.2 * safetyLevel) * GREEN[2]));
        }

        if (warnLevel > 5) {
            int danger = warnLevel - 5;
            int safetyLevel = 5 - danger;
            c = new Color(display, (int) ((0.2 * danger) * RED[0] +  (0.2 * safetyLevel) * YELLOW[0]),
                                   (int) ((0.2 * danger) * RED[1] +  (0.2 * safetyLevel) * YELLOW[1]),
                                   (int) ((0.2 * danger) * RED[2] +  (0.2 * safetyLevel) * YELLOW[2]));
        }

        gc.setBackground(c);
        gc.fillRectangle(alerter.getBounds());

        if (wl.getDelta() > 0) {
            up(display);
        }
        if (wl.getDelta() < 0) {
            down(display);
        }
        if (!wl.isMarketOpen()) {
            Color c1 = new Color(display, 0xFF, 0xFF, 0xFF);
            gc.setForeground(c1);

            gc.drawLine(2, 2, 13, 13);
            gc.drawLine(3, 2, 13, 12);
            gc.drawLine(2, 3, 12, 13);
            gc.drawLine(12, 2, 2, 12);
            gc.drawLine(13, 2, 2, 13);
            gc.drawLine(13, 3, 3, 13);
        }

        boolean needRefresh = (currentLevel.get() != warnLevel) ||
                              (wl.isMarketOpen() != currentMarket.get()) ||
                              (wl.getDelta() != currentDelta.get());
        currentDelta.set(wl.getDelta());
        currentLevel.set(wl.getLevel());
        currentMarket.set(wl.isMarketOpen());
        return needRefresh;
    }

    private void up(Display display) {
        Color c = new Color(display, 0, 0, 0);
        gc.setForeground(c);
        gc.drawLine(7, 5, 3, 9);
        gc.drawLine(8, 5, 12, 9);
        gc.drawLine(3, 9, 12, 9);
    }

    private void down(Display display) {
        Color c = new Color(display, 0, 0, 0);
        gc.setForeground(c);
        gc.drawLine(3, 6, 7, 10);
        gc.drawLine(8, 10, 12, 6);
        gc.drawLine(3, 6, 12, 6);
    }

    public void offline(Display display) {
        Color c = new Color(display, 0, 0, 0);
        gc.setBackground(c);
        gc.fillRectangle(alerter.getBounds());
        currentLevel.set(-1);
        currentMarket.set(false);
        currentDelta.set(0);
    }

    public void dispose() {
        gc.dispose();
    }

    public Image getImage() {
        return alerter;
    }


}
