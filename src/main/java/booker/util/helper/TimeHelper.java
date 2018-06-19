package booker.util.helper;

import booker.util.enums.state.UnitTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public static int getUnitTimeValue(UnitTime unitTime, LocalDateTime localDateTime) {
        if (unitTime.equals(UnitTime.MONTH)) {
            return localDateTime.getMonthValue();
        } else if (unitTime.equals(UnitTime.QUARTER)) {
            int month = localDateTime.getMonthValue();
            if (month <= 3) {
                return 1;
            } else if (month <= 6) {
                return 2;
            } else if (month <= 9) {
                return 3;
            } else {
                return 4;
            }
        } else {
            return localDateTime.getYear();
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
