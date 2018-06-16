package booker.util.helper;

import java.time.LocalDate;

public class TimeHelper {

    private TimeHelper() {
    }

    public static int getMonthLength(int year, int month) {
        return LocalDate.of(year, month, 1).lengthOfMonth();
    }

    public static int getQuarterLength(int year, int quarter) {
        switch (quarter) {
            case 1:
                if (isLeap(year)) {
                    return 90;
                } else {
                    return 91;
                }
            case 2:
                return 91;
            case 3:
                return 92;
            case 4:
                return 92;
            default:
                break;
        }
        return 0;
    }

    public static int getYearLength(int year) {
        if (isLeap(year)) {
            return 366;
        } else {
            return 365;
        }
    }

    private static boolean isLeap(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
