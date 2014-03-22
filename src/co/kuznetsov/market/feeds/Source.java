package co.kuznetsov.market.feeds;

import co.kuznetsov.market.monitor.Ticker;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public interface Source {

    BigDecimal getCurrent() throws IOException;
    Ticker getTicker();

}
