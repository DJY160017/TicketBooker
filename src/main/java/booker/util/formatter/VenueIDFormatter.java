package booker.util.formatter;

public class VenueIDFormatter {

    public static int deFormate(String num) {
        return Integer.parseInt(num);
    }

    public static String formate(int num) {
        StringBuilder str_num = new StringBuilder(String.valueOf(num));
        for (int i = str_num.length(); i < 7; i++) {
            str_num.insert(0, "0");
        }
        return str_num.toString();
    }
}
