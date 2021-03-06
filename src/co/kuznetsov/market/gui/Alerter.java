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

    private int [] RED      = {0xCC, 0x45, 0};
    private int [] YELLOW   = {0xFF, 0xFF, 0x66};
    private int [] GREEN    = {0,    0xCC, 0};

    private AtomicInteger currentDelta  = new AtomicInteger(0);
    private AtomicInteger currentLevel  = new AtomicInteger(0);
    private AtomicBoolean currentMarket = new AtomicBoolean(false);


    public Alerter(Display display) {
        offline(display);
    }

    public Image status(Display display, WarnLevel wl) {
        boolean needRefresh = (wl.getLevel() != currentLevel.get()) ||
                (wl.isMarketOpen() != currentMarket.get()) ||
                (wl.getDelta() != currentDelta.get());
        currentDelta.set(wl.getDelta());
        currentLevel.set(wl.getLevel());
        currentMarket.set(wl.isMarketOpen());

        if (!needRefresh) {
            return null;
        }

        Image img = new Image(display, 16, 16);
        GC gc = new GC(img);

        Color    c = getWarningColor(display, wl);
        Color cInv = invertColor(display, c);

        gc.setBackground(c);
        gc.fillRectangle(img.getBounds());

        if (!wl.isMarketOpen()) {
            marketClosed(gc, cInv);
        } else {
            if (wl.getDelta() > 0) {
                up(gc, cInv);
            }
            if (wl.getDelta() < 0) {
                down(gc, cInv);
            }
            if (wl.getDelta() == 0) {
                flat(gc, cInv);
            }
        }

        gc.dispose();
        return img;
    }

    private Color invertColor(Display display, Color c) {
        return new Color(display, 255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    private Color getWarningColor(Display display, WarnLevel wl) {
        Color c = new Color(display, YELLOW[0], YELLOW[1], YELLOW[2]);
        int warnLevel = wl.getLevel();

        if (warnLevel < 5) {
            int safetyLevel = 5 - warnLevel;
            c = new Color(display, (int) ((0.2 * warnLevel) * YELLOW[0] +  (0.2 * safetyLevel) * GREEN[0]),
                                   (int) ((0.2 * warnLevel) * YELLOW[1] +  (0.2 * safetyLevel) * GREEN[1]),
                                   (int) ((0.2 * warnLevel) * YELLOW[2] +  (0.2 * safetyLevel) * GREEN[2]));
        }
        if (warnLevel > 5) {
            int danger = warnLevel - 5;
            int safetyLevel = 5 - danger;
            c = new Color(display, (int) ((0.2 * danger) * RED[0] +  (0.2 * safetyLevel) * YELLOW[0]),
                                   (int) ((0.2 * danger) * RED[1] +  (0.2 * safetyLevel) * YELLOW[1]),
                                   (int) ((0.2 * danger) * RED[2] +  (0.2 * safetyLevel) * YELLOW[2]));
        }
        return c;
    }

    private void marketClosed(GC gc, Color с) {
        gc.setForeground(с);

        gc.drawLine(2, 2, 13, 13);
        gc.drawLine(3, 2, 13, 12);
        gc.drawLine(2, 3, 12, 13);

        gc.drawLine(12, 2, 2, 12);
        gc.drawLine(13, 2, 2, 13);
        gc.drawLine(13, 3, 3, 13);
    }

    private void up(GC gc, Color c) {
        gc.setForeground(c);
        gc.drawLine(12, 2, 2, 12);
        gc.drawLine(13, 2, 2, 13);
        gc.drawLine(13, 3, 3, 13);

        gc.drawLine(7, 2, 13, 2);
        gc.drawLine(7, 3, 13, 3);
        gc.drawLine(12, 8, 12, 2);
        gc.drawLine(13, 8, 13, 2);
    }

    private void down(GC gc, Color c) {
        gc.setForeground(c);
        gc.drawLine(2, 2, 13, 13);
        gc.drawLine(3, 2, 13, 12);
        gc.drawLine(2, 3, 12, 13);

        gc.drawLine(13, 7, 13, 13);
        gc.drawLine(12, 7, 12, 13);
        gc.drawLine(7, 13, 13, 13);
        gc.drawLine(7, 12, 13, 12);
    }

    private void flat(GC gc, Color c) {
        gc.setForeground(c);

        gc.drawLine(2, 7, 13, 7);
        gc.drawLine(2, 8, 13, 8);
    }

    public Image offline(Display display) {
        Image img = new Image(display, 16, 16);
        GC gc = new GC(img);
        Color c = new Color(display, 0, 0, 0);
        gc.setBackground(c);
        gc.fillRectangle(img.getBounds());
        currentLevel.set(-1);
        currentMarket.set(false);
        currentDelta.set(0);
        return img;
    }

}
