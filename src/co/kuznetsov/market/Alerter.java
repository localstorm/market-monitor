package co.kuznetsov.market;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class Alerter {

    private int [] RED      = {0xFF, 0x45, 0};
    private int [] YELLOW   = {0xFF, 0xFF, 0x66};
    private int [] GREEN    = {0, 0x80, 0};

    private Image alerter;
    private GC gc;
    private int dayOpenLevel = 0;

    public Alerter(Display display) {
        alerter = new Image(display, 16, 16);
        gc = new GC(alerter);
        offline(display);
    }

    public void status(Display display, int warnLevel) {
        Color c = null;
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
        if (warnLevel > dayOpenLevel) {
            up(display);
        } else {
            down(display);
        }


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
    }

    public void dispose() {
        gc.dispose();
    }

    public Image getImage() {
        return alerter;
    }


}
