package booker.util.exception;

/**
 * Created by Harvey on 2017/3/11.
 */
public class TicketCheckedException extends Exception {


    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "该票已检票登记，请勿重复检票";
    }
}
