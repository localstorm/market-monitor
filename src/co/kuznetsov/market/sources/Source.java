package co.kuznetsov.market.sources;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public interface Source {

    BigDecimal getCurrent() throws IOException;

}
