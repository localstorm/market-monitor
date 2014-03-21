package co.kuznetsov.market.market;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author localstorm
 *         Date: 21.03.14
 */
public class SpreadsParser {
    public List<Spread> parse(File file) throws IOException {
        List<Spread> result = new ArrayList<Spread>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }

            String[] spreads = line.split(":");
            if (spreads.length != 2) {
                throw new IOException("Incorrect spread: [" + line + "]");
            }
            String ticker = spreads[0];

            try {
                Tickers t = Tickers.valueOf(ticker);
                String[] lohi = spreads[1].split("-");
                if (lohi.length != 2) {
                    throw new IllegalArgumentException("Incorrect spread: [" + spreads[1] + "]");
                }

                BigDecimal lo = new BigDecimal(lohi[0]);
                BigDecimal hi = new BigDecimal(lohi[1]);
                result.add(new Spread(t.name(), lo, hi));
            } catch (Exception e) {
                throw new IOException("Problematic line: " + line, e);
            }
        }
        return result;
    }
}
