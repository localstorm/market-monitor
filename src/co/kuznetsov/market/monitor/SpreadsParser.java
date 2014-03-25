package co.kuznetsov.market.monitor;

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
                Ticker t = Ticker.valueOf(ticker);
                String[] lohi = spreads[1].split("-");

                BigDecimal lo, hi, mid;
                switch(lohi.length) {
                    case 3:
                        lo  = new BigDecimal(lohi[0]);
                        mid = new BigDecimal(lohi[1]);
                        hi  = new BigDecimal(lohi[2]);
                        break;
                    case 2:
                        lo  = new BigDecimal(lohi[0]);
                        hi  = new BigDecimal(lohi[1]);
                        mid = null;
                        break;
                    default:
                        throw new IllegalArgumentException("Incorrect spread: [" + line + "]");
                }
                result.add(new Spread(t, lo, mid, hi));
            } catch (Exception e) {
                throw new IOException("Problematic line: " + line, e);
            }
        }
        return result;
    }
}
