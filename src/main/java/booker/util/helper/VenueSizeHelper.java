package booker.util.helper;

import java.util.HashMap;
import java.util.Map;

public class VenueSizeHelper {

    private VenueSizeHelper() {
    }

    public static Map<String, Integer> getSizeParm(String size) {
        Map<String, Integer> result = new HashMap<>();
        if (size.equals("小")) {
            result.put("min_col", 5);
            result.put("min_raw", 5);
            result.put("max_raw", 10);
            result.put("max_col", 10);
        } else {
            result.put("min_col", 11);
            result.put("min_raw", 11);
            result.put("max_raw", 16);
            result.put("max_col", 16);
        }
        return result;
    }

    public static String getSize(int col, int raw) {
        if (col <= 10 && col >= 5 && raw <= 10 && raw >= 5) {
            return "小";
        } else {
            return "中";
        }
    }
}
