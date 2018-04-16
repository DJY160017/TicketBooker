package booker.util.formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Byron Dong on 2017/4/18.
 */
public class NumberFormatter {

    public static double rounding(double num, int length) {
        BigDecimal b = new BigDecimal(num);
        return b.setScale(length, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String progressFormat(double num) {
        DecimalFormat df = new DecimalFormat("#0" + "%");
        return df.format(num);
    }

    public static String percentFormat(double num, int length) {
        StringBuilder pattern = new StringBuilder("#0.");

        for (int i = 0; i < length; i++) {
            pattern.append("0");
        }
        pattern.append("%");
        DecimalFormat df = new DecimalFormat(pattern.toString());

        return df.format(num);
    }

    public static String decimaFormat(double num, int length) {
        StringBuilder pattern = new StringBuilder("#0.");

        for (int i = 0; i < length; i++) {
            pattern.append("0");
        }
        DecimalFormat df = new DecimalFormat(pattern.toString());

        return df.format(num);
    }
}
