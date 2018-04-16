package booker.util.exception;

public class BalanceInsufficientException extends Exception {

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "当前账户的余额不足，不能完成支付";
    }
}
