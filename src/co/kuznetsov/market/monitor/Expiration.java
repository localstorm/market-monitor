package co.kuznetsov.market.monitor;

import java.util.Calendar;
import java.util.Date;

/**
 * @author localstorm
 *         Date: 3/27/14
 */
public class Expiration {
    private static final String WEEKLY_EXPIRATION  = "([jJ]an|[fF]eb|[mM]ar|[aA]pr|[mM]ay|[jJ]un|[jJ]ul|[aA]ug|[sS]ep|[oO]ct|[nN]ov|[dD]ec)\\d{2}[wW][kK](1|2|4)";
    private static final String MONTHLY_EXPIRATION = "([jJ]an|[fF]eb|[mM]ar|[aA]pr|[mM]ay|[jJ]un|[jJ]ul|[aA]ug|[sS]ep|[oO]ct|[nN]ov|[dD]ec)\\d{2}";

    private int month;
    private int year;
    private int week;

    public Expiration(String expString) {
        if (expString.matches(MONTHLY_EXPIRATION)) {
            week = 3;
        } else if (expString.matches(WEEKLY_EXPIRATION)) {
            week =  Integer.parseInt(expString.substring(7, 8));
        }
        month = Month.valueOf(expString.substring(0, 3).toUpperCase()).ordinal() + 1;
        year  = Integer.parseInt(expString.substring(3, 5));
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

    public int daysLeft() {
        Calendar tod = Calendar.getInstance();
        tod.set(Calendar.HOUR_OF_DAY, 0);
        tod.set(Calendar.MINUTE, 0);
        tod.set(Calendar.SECOND, 0);
        tod.set(Calendar.MILLISECOND, 0);
        Date today = tod.getTime();
        Date expiration = getExpirationDate();
        double frac = (expiration.getTime() - today.getTime()) / (1000.0 * 60 * 60 * 24);
        return (int) Math.round(frac);
    }

    public Date getExpirationDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2000 + year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 15);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        int fridaysCount = (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) ? 1: 0;

        while (fridaysCount < week) {
            c.add(Calendar.DATE, 1);
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                fridaysCount++;
            }
        }
        return c.getTime();
    }

    @Override
    public String toString() {
        String _month = Month.values()[month - 1].name();
        return _month.charAt(0) + _month.substring(1).toLowerCase() + year + "Wk" + week;
    }

    private static enum Month {
        JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
    }
}
