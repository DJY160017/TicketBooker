package booker.util.exception;

public class IntegralInsufficientException extends Exception {

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "当前账户的积分不足，不能完成兑换";
    }
}
