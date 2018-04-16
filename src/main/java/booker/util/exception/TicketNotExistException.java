package booker.util.exception;

/**
 * Created by Harvey on 2017/3/11.
 */
public class TicketNotExistException extends Exception {


    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "该票不存在，请仔细检查";
    }
}
