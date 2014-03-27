package co.kuznetsov.market;

import co.kuznetsov.market.monitor.Expiration;

/**
 * @author localstorm
 *         Date: 3/27/14
 */
public class Test {
    public static void main(String[] args) {
        Expiration e = new Expiration("Apr14");
        System.out.println(e.getExpirationDate()+":"+e.daysLeft());
    }
}
